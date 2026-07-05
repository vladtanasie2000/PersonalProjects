package com.example.WouldILie.entities;

import java.util.List;

import org.hibernate.type.YesNoConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "Questions")
public class QuestionEntity {

  @Id
  @Column(name = "question_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questions_seq")
  @SequenceGenerator(name = "questions_seq", sequenceName = "questions_seq", allocationSize = 1)
  private Long id;

  @Column(name = "question_value", length = 150)
  String question;

  @ManyToOne
  @JoinColumn(name = "user_id")
  UserEntity user;

  @Column(name = "points")
  Integer points;

  @Convert(converter = YesNoConverter.class)
  Boolean isTrue;

  @Convert(converter = YesNoConverter.class)
  Boolean wasUsed;

  @Convert(converter = YesNoConverter.class)
  Boolean isActive;

  @OneToMany(mappedBy = "qEntity")
  List<VoteEntity> vote;

  public List<VoteEntity> getVote() {
    return vote;
  }

  public void setVote(List<VoteEntity> vote) {
    this.vote = vote;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public Integer getPoints() {
    return points;
  }

  public Boolean getIsTrue() {
    return isTrue;
  }

  public void setIsTrue(Boolean isTrue) {
    this.isTrue = isTrue;
  }

  public Boolean getWasUsed() {
    return wasUsed;
  }

  public void setWasUsed(Boolean wasUsed) {
    this.wasUsed = wasUsed;
  }

  public void setPoints(Integer points) {
    this.points = points;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

}
