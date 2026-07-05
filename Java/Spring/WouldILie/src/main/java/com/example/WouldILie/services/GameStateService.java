package com.example.WouldILie.services;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

import com.example.WouldILie.entities.QuestionEntity;

@Service
public class GameStateService {

  // this should use atomic
  private AtomicReference<Long> lastEndedQuestionId = new AtomicReference<>();

  public void setLastEndedQuestion(QuestionEntity question) {
    lastEndedQuestionId.set(question.getId());
  }

  public Optional<Long> getLastEndedQuestionId() {
    return Optional.ofNullable(lastEndedQuestionId.get());
  }
}
