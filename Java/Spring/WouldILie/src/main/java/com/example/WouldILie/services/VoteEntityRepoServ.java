package com.example.WouldILie.services;

import java.util.List;

import org.springframework.stereotype.Service;
import com.example.WouldILie.entities.QuestionEntity;
import com.example.WouldILie.entities.TeamEntity;
import com.example.WouldILie.entities.UserEntity;
import com.example.WouldILie.entities.VoteEntity;
import com.example.WouldILie.repos.VoteEntityRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class VoteEntityRepoServ {
  VoteEntityRepo VoteEntityRepo;

  public VoteEntityRepoServ(com.example.WouldILie.repos.VoteEntityRepo voteEntityRepo) {
    VoteEntityRepo = voteEntityRepo;
  }

  public VoteEntity findById(Long id) {
    return VoteEntityRepo.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  public List<VoteEntity> findAll() {
    return VoteEntityRepo.findAll();
  }

  public void save(VoteEntity voteEntity) {
    VoteEntityRepo.save(voteEntity);
  }

  public List<VoteEntity> findAllByTeam(TeamEntity teamEntity) {
    return VoteEntityRepo.findAllByTeam(teamEntity).orElseThrow(EntityNotFoundException::new);
  }

  public List<VoteEntity> findAllByUser(UserEntity userEntity) {
    return VoteEntityRepo.findAllByUser(userEntity).orElseThrow(EntityNotFoundException::new);
  }

  public Boolean getVotedOnce(QuestionEntity questionEntity, UserEntity userEntity) {
    return VoteEntityRepo.getVotedOnce(questionEntity, userEntity).orElseThrow(EntityNotFoundException::new);

  }

  public List<VoteEntity> findAllByQuestion(QuestionEntity questionEntity) {
    return VoteEntityRepo.findAllByQuestion(questionEntity).orElseThrow(EntityNotFoundException::new);
  }

}
