package com.example.WouldILie.services;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.WouldILie.entities.QuestionEntity;
import com.example.WouldILie.entities.TeamEntity;
import com.example.WouldILie.entities.VoteEntity;
import com.example.WouldILie.repos.TeamEntityRepo;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TeamEntityRepoServ {
  TeamEntityRepo teamEntityRepo;
  UserEntityRepoServ userEntityRepoServ;
  QuestionEntityRepoServ questionEntityRepoServ;

  public TeamEntityRepoServ(TeamEntityRepo teamEntityRepo, UserEntityRepoServ userEntityRepoServ,
      QuestionEntityRepoServ questionEntityRepoServ) {
    this.teamEntityRepo = teamEntityRepo;
    this.userEntityRepoServ = userEntityRepoServ;
    this.questionEntityRepoServ = questionEntityRepoServ;
  }

  public TeamEntity save(TeamEntity teamEntity) {
    return teamEntityRepo.save(teamEntity);
  }

  public List<TeamEntity> findAll() {
    return teamEntityRepo.findAll();
  }

  public TeamEntity findById(Long id) {
    return teamEntityRepo.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  public TeamEntity findByTeamName(String name) {
    return teamEntityRepo.findByTeamName(name).orElseThrow(EntityNotFoundException::new);
  }

  public Long count() {
    return teamEntityRepo.count();
  }

  public List<VoteEntity> findVotes(TeamEntity teamEntity) {
    return teamEntityRepo.findVotes(teamEntity).orElseThrow(EntityNotFoundException::new);
  }

  private List<Object[]> findStats(TeamEntity teamEntity) {
    return teamEntityRepo.findStats(teamEntity);
  }

  public TeamEntity findOpposingTeamByQuestion(QuestionEntity questionEntity) {
    return teamEntityRepo.findOpposingTeamByQuestion(questionEntity).orElseThrow(EntityNotFoundException::new);
  }

  public int findTeamScore(TeamEntity teamEntity) {
    List<Object[]> rows = findStats(teamEntity);
    int score = 0;
    for (Object[] row : rows) {
      Boolean isCorrect = (Boolean) row[1];
      Long points = ((Number) row[2]).longValue();
      Long totalVotes = ((Number) row[3]).longValue();
      Long yesVotes = ((Number) row[4]).longValue();
      boolean teamAnswer = yesVotes > (totalVotes / 2.0);
      if (teamAnswer == Boolean.TRUE.equals(isCorrect)) {
        score += points;
      }
    }
    return score;
  }
}
