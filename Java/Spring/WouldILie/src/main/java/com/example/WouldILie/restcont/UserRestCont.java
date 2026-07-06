package com.example.WouldILie.restcont;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.WouldILie.dtos.admin.ResultDTO;
import com.example.WouldILie.dtos.admin.StateDTO;
import com.example.WouldILie.dtos.votes.VotesDTO;
import com.example.WouldILie.entities.QuestionEntity;
import com.example.WouldILie.entities.TeamEntity;
import com.example.WouldILie.entities.UserEntity;
import com.example.WouldILie.entities.VoteEntity;
import com.example.WouldILie.services.GameStateService;
import com.example.WouldILie.services.QuestionEntityRepoServ;
import com.example.WouldILie.services.TeamEntityRepoServ;
import com.example.WouldILie.services.UserEntityRepoServ;
import com.example.WouldILie.services.VoteEntityRepoServ;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/user")
public class UserRestCont {

  private final QuestionEntityRepoServ questionEntityRepoServ;
  private final UserEntityRepoServ userEntityRepoServ;
  private final VoteEntityRepoServ voteEntityRepoServ;
  private final GameStateService gameStateService;
  private final TeamEntityRepoServ teamEntityRepoServ;

  public UserRestCont(
      QuestionEntityRepoServ questionEntityRepoServ,
      UserEntityRepoServ userEntityRepoServ,
      VoteEntityRepoServ voteEntityRepoServ,
      TeamEntityRepoServ teamEntityRepoServ,
      GameStateService gameStateService) {
    this.questionEntityRepoServ = questionEntityRepoServ;
    this.userEntityRepoServ = userEntityRepoServ;
    this.voteEntityRepoServ = voteEntityRepoServ;
    this.teamEntityRepoServ = teamEntityRepoServ;
    this.gameStateService = gameStateService;
  }

  @GetMapping
  public ResponseEntity<?> showCurrentQuestion(Principal principal) {
    Optional<QuestionEntity> activeQuestion = questionEntityRepoServ.findByIsActive(Boolean.TRUE);
    if (activeQuestion.isEmpty()) {
      return ResponseEntity.ok("No active question");
    }
    UserEntity user = userEntityRepoServ.findByName(principal.getName());
    boolean alreadyVoted = voteEntityRepoServ.getVotedOnce(activeQuestion.get(), user);
    StateDTO stateDTO = new StateDTO(
        principal.getName(),
        activeQuestion.get().getId(),
        activeQuestion.get().getQuestion(),
        activeQuestion.get().getUser().getName(),
        alreadyVoted);

    return ResponseEntity.ok(stateDTO);
  }

  @PostMapping("/vote")
  @Transactional
  public ResponseEntity<String> vote(@RequestBody VotesDTO votesDTO, Principal principal) {
    Optional<QuestionEntity> activeQuestion = questionEntityRepoServ.findByIsActive(Boolean.TRUE);
    if (activeQuestion.isEmpty()) {
      return ResponseEntity.ok("No active question");
    }
    UserEntity voter = userEntityRepoServ.findByName(principal.getName());
    TeamEntity voterTeam = voter.getTeam();
    TeamEntity ownerTeam = activeQuestion.get().getUser().getTeam();

    if (voterTeam == null) {
      return ResponseEntity.badRequest().body("You are not assigned to a team");
    }

    if (ownerTeam == null) {
      return ResponseEntity.badRequest().body("Question owner is not assigned to a team");
    }

    if (voterTeam.getId().equals(ownerTeam.getId())) {
      return ResponseEntity.badRequest().body("You cannot vote for your own team's question");
    }

    if (voteEntityRepoServ.getVotedOnce(activeQuestion.get(), voter)) {
      return ResponseEntity.badRequest().body("You already voted");
    }

    VoteEntity vote = new VoteEntity();
    vote.setTrue(votesDTO.isTrue());
    vote.setUser(voter);
    vote.setqEntity(activeQuestion.get());

    voteEntityRepoServ.save(vote);

    return ResponseEntity.ok("Vote saved");
  }

  @GetMapping("/result")
  public ResponseEntity<?> viewResult(Principal principal) {
    Optional<Long> lastQuestionIdOpt = gameStateService.getLastEndedQuestionId();
    if (lastQuestionIdOpt.isEmpty()) {
      return ResponseEntity.ok("No question has ended yet");
    }
    QuestionEntity question = questionEntityRepoServ.findById(lastQuestionIdOpt.get())
        .orElseThrow(EntityNotFoundException::new);
    TeamEntity votingTeam = teamEntityRepoServ.findOpposingTeamByQuestion(question);
    List<VoteEntity> votes = question.getVote();
    int requiredVotes = votingTeam.getTeamMembers().size();
    if (votes.size() < requiredVotes) {
      return ResponseEntity.ok("Not everyone voted");
    }
    Long yesVotes = votes.stream()
        .filter(VoteEntity::isTrue)
        .count();
    boolean votedYes = yesVotes > votes.size() / 2.0;
    String sVotedYes = votedYes ? "Voted Yes" : "Voted No";
    ResultDTO resultDTO = new ResultDTO(
        question.getId(),
        question.getQuestion(),
        sVotedYes,
        question.getIsTrue(),
        yesVotes,
        Long.valueOf(votes.size()));
    return ResponseEntity.ok(resultDTO);
  }

  @GetMapping("/score/team")
  public ResponseEntity<?> viewTeamScore(Principal principal) {
    UserEntity user = userEntityRepoServ.findByName(principal.getName());
    TeamEntity team = user.getTeam();
    if (team == null) {
      return ResponseEntity.badRequest().body("You are not assigned to a team");
    }
    Map<String, Object> score = new LinkedHashMap<>();
    score.put("teamName", team.getTeamName());
    score.put("teamPoints", team.getTeamPoints());

    return ResponseEntity.ok(score);
  }

  @GetMapping("/score/me")
  public ResponseEntity<?> viewOwnScore(Principal principal) {
    UserEntity user = userEntityRepoServ.findByName(principal.getName());
    Map<String, Object> score = new LinkedHashMap<>();
    score.put("name", user.getName());
    score.put("teamName", user.getTeam() != null ? user.getTeam().getTeamName() : null);
    // should also set userScore
    score.put("userPoints", userEntityRepoServ.getUserScore(user));

    return ResponseEntity.ok(score);
  }
}
