package com.example.WouldILie.restcont;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.WouldILie.dtos.admin.AdminQuestionDTO;
import com.example.WouldILie.dtos.admin.AdminStateDTO;
import com.example.WouldILie.dtos.admin.AdminTeamDTO;
import com.example.WouldILie.dtos.admin.AdminUserDTO;
import com.example.WouldILie.entities.QuestionEntity;
import com.example.WouldILie.entities.TeamEntity;
import com.example.WouldILie.entities.UserEntity;
import com.example.WouldILie.entities.VoteEntity;
import com.example.WouldILie.services.GameStateService;
import com.example.WouldILie.services.QuestionEntityRepoServ;
import com.example.WouldILie.services.TeamEntityRepoServ;
import com.example.WouldILie.services.UserEntityRepoServ;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/admin")
public class AdminRestContr {
  TeamEntityRepoServ teamEntityRepoServ;
  QuestionEntityRepoServ questionEntityRepoServ;
  UserEntityRepoServ userEntityRepoServ;
  private final GameStateService gameStateService;

  public AdminRestContr(TeamEntityRepoServ teamEntityRepoServ, QuestionEntityRepoServ questionEntityRepoServ,
      UserEntityRepoServ userEntityRepoServ, GameStateService gameStateService) {
    this.teamEntityRepoServ = teamEntityRepoServ;
    this.questionEntityRepoServ = questionEntityRepoServ;
    this.userEntityRepoServ = userEntityRepoServ;
    this.gameStateService = gameStateService;
  }

  @GetMapping("state")
  public AdminStateDTO getState() {
    List<AdminQuestionDTO> allQuestions = questionEntityRepoServ.findAll().stream().map(
        (question) -> new AdminQuestionDTO(question.getId(), question.getQuestion(), question.getIsTrue(),
            question.getPoints(), question.getUser().getName(), question.getWasUsed(), question.getIsActive()))
        .toList();
    List<AdminUserDTO> allUsers = userEntityRepoServ.findAll().stream()
        .map(user -> new AdminUserDTO(user.getId(), user.getName(),
            user.getTeam() == null ? "null" : user.getTeam().getTeamName()))
        .toList();
    List<AdminTeamDTO> allTeams = teamEntityRepoServ.findAll().stream().map(team -> new AdminTeamDTO(team.getId(),
        team.getTeamName(), team.getTeamPoints(),
        team.getTeamMembers() == null ? new ArrayList<>()
            : team.getTeamMembers().stream().map(s -> s.getName()).toList()))
        .toList();
    return new AdminStateDTO(allUsers, allTeams, allQuestions);
  }

  @PostMapping("/questions/add")
  @Transactional
  public String addQuestion(@RequestBody AdminQuestionDTO form) {
    UserEntity user = userEntityRepoServ.findByName(form.userName());
    QuestionEntity question = new QuestionEntity();
    question.setQuestion(form.question());
    question.setIsTrue(form.isTrue());
    question.setPoints(form.points());
    question.setUser(user);
    question.setWasUsed(false);
    question.setIsActive(false);
    questionEntityRepoServ.save(question);
    return "Question added";
  }

  @PostMapping("/teams/assign-users")
  @Transactional
  public String assignUsersToExistingTeam(@RequestBody AdminTeamDTO form) {
    TeamEntity team = teamEntityRepoServ.findByTeamName(form.teamName());

    List<UserEntity> users = form.users() == null
        ? new ArrayList<>()
        : form.users()
            .stream()
            .map(userEntityRepoServ::findByName)
            .collect(Collectors.toCollection(ArrayList::new));

    for (UserEntity user : users) {
      user.setTeam(team);
    }
    userEntityRepoServ.saveAll(users);
    return "Users assigned to team";
  }

  @PostMapping("/teams/add")
  @Transactional
  public String createTeam(@RequestBody AdminTeamDTO form) {
    if (teamEntityRepoServ.count() >= 2) {
      return "Only two teams are allowed!";
    }
    TeamEntity team = new TeamEntity();
    team.setTeamName(form.teamName());
    team.setTeamPoints(form.points() != null ? form.points() : 0);
    TeamEntity savedTeam = teamEntityRepoServ.save(team);
    List<UserEntity> users = form.users() == null
        ? new ArrayList<>()
        : form.users()
            .stream()
            .map(userEntityRepoServ::findByName)
            .collect(Collectors.toCollection(ArrayList::new));
    for (UserEntity user : users) {
      user.setTeam(savedTeam);
    }
    userEntityRepoServ.saveAll(users);
    return "Team created";
  }

  @PostMapping("/questions/active")
  @Transactional
  public String setActiveQuestion(@RequestBody AdminQuestionDTO form) {
    QuestionEntity activeQuestion = questionEntityRepoServ.findById(form.id())
        .orElseThrow(() -> new EntityNotFoundException("Question not found with id: " + form.id()));
    List<QuestionEntity> allQuestions = questionEntityRepoServ.findAll();
    allQuestions.remove(activeQuestion);
    if (activeQuestion.getWasUsed().equals(Boolean.TRUE)) {
      return "Question was already used";
    }
    for (QuestionEntity question : allQuestions) {
      question.setIsActive(false);
    }
    allQuestions.add(activeQuestion);
    activeQuestion.setIsActive(true);
    activeQuestion.setWasUsed(true);
    questionEntityRepoServ.saveAll(allQuestions);
    return "Active question set";
  }

  @PostMapping("/questions/mark-used")
  @Transactional
  public String endQuestion() {
    QuestionEntity question = questionEntityRepoServ.findByIsActive(Boolean.TRUE).orElseThrow();
    TeamEntity votingTeam = teamEntityRepoServ.findOpposingTeamByQuestion(question);
    List<VoteEntity> votes = question.getVote();
    int requiredVotes = votingTeam.getTeamMembers().size();
    if (votes.size() < requiredVotes) {
      return "Not everyone voted";
    }
    // update score
    int score = teamEntityRepoServ.findTeamScore(votingTeam);
    votingTeam.setTeamPoints(score);
    // update question
    question.setWasUsed(true);
    question.setIsActive(false);
    teamEntityRepoServ.save(votingTeam);
    questionEntityRepoServ.save(question);
    gameStateService.setLastEndedQuestion(question);
    return "Question ended";
  }
}
