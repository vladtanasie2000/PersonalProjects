package com.example.WouldILie.services;

import java.util.Optional;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.WouldILie.entities.QuestionEntity;
import com.example.WouldILie.entities.UserEntity;
import com.example.WouldILie.entities.VoteEntity;
import com.example.WouldILie.repos.QuestionEntityRepo;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class QuestionEntityRepoServ {
  QuestionEntityRepo questionEntityRepo;

  public QuestionEntityRepoServ(QuestionEntityRepo questionEntityRepo) {
    this.questionEntityRepo = questionEntityRepo;
  }

  public Optional<QuestionEntity> findById(Long id) {
    return questionEntityRepo.findById(id);
  }

  public List<QuestionEntity> findAllUserQuestions(UserEntity userEntity) {
    return questionEntityRepo.findByUser(userEntity).orElseThrow(EntityNotFoundException::new);
  }

  public Boolean isQuestionTrue(Long questionId) {
    return questionEntityRepo.isQuestionTrue(questionId).orElseThrow(EntityNotFoundException::new);
  }

  public List<QuestionEntity> findAll() {
    return questionEntityRepo.findAll();
  }

  public Long max() {
    return questionEntityRepo.max();
  }

  public Long count() {
    return questionEntityRepo.count();
  }

  @Transactional
  public void save(QuestionEntity questionEntity) {
    questionEntityRepo.save(questionEntity);
  }

  @Transactional
  public void saveAll(Iterable<QuestionEntity> questions) {
    questionEntityRepo.saveAll(questions);
  }

  public Optional<QuestionEntity> findByIsActive(Boolean isActive) {
    return questionEntityRepo.findByIsActive(isActive);
  }

  public List<QuestionEntity> findAllByVotes(List<VoteEntity> votes) {
    return questionEntityRepo.findAllByVotes(votes).orElseThrow(EntityNotFoundException::new);
  }

}
