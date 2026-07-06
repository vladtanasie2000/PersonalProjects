package com.example.WouldILie.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.example.WouldILie.entities.QuestionEntity;
import com.example.WouldILie.entities.UserEntity;
import com.example.WouldILie.repos.QuestionEntityRepo;
import com.example.WouldILie.repos.UserEntityRepo;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserEntityRepoServ {
  UserEntityRepo userEntityRepo;
  QuestionEntityRepo questionEntityRepo;

  public void addUserQuestion(QuestionEntity qEntity, UserEntity user) {
    qEntity.setUser(user);
    user.getQuestions().add(qEntity);
    questionEntityRepo.save(qEntity);
    userEntityRepo.save(user);

  }

  public UserEntityRepoServ(UserEntityRepo userEntityRepo, QuestionEntityRepo questionEntityRepo) {
    this.userEntityRepo = userEntityRepo;
    this.questionEntityRepo = questionEntityRepo;
  }

  public Optional<UserEntity> findById(Long userId) {
    return userEntityRepo.findById(userId);
  }

  public void save(UserEntity userEntity) {
    userEntityRepo.save(userEntity);
  }

  public List<UserEntity> findAll() {
    return userEntityRepo.findAll();
  }

  public void saveAll(Iterable<UserEntity> list) {
    userEntityRepo.saveAll(list);
  }

  public UserEntity findByName(String name) {
    return userEntityRepo.findByName(name).orElseThrow(EntityNotFoundException::new);
  }

  public List<UserEntity> getAllUserFromTeamName(String teamName) {
    return userEntityRepo.findAllByUsername(teamName).orElseThrow(EntityNotFoundException::new);
  }

  public boolean userExists(String name) {
    return userEntityRepo.userExist(name);
  }

  public Long getUserScore(UserEntity userEntity) {
    return userEntityRepo.findScore(userEntity);
  }
}
