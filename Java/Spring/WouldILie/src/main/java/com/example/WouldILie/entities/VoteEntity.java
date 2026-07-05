package com.example.WouldILie.entities;

import org.hibernate.type.YesNoConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "Votes",
    // one vote per question and user
    // can have multiple votes but not from same user
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "question_id" })
    })
public class VoteEntity {

  @Id
  @Column(name = "vote_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "votes_seq")
  @SequenceGenerator(name = "votes_seq", sequenceName = "votes_seq", allocationSize = 1)
  Long id;

  @ManyToOne
  @JoinColumn(name = "question_id")
  QuestionEntity qEntity;

  @ManyToOne
  @JoinColumn(name = "user_id")
  UserEntity user;

  @Convert(converter = YesNoConverter.class)
  boolean isTrue;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public QuestionEntity getqEntity() {
    return qEntity;
  }

  public void setqEntity(QuestionEntity qEntity) {
    this.qEntity = qEntity;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public boolean isTrue() {
    return isTrue;
  }

  public void setTrue(boolean isTrue) {
    this.isTrue = isTrue;
  }
}
