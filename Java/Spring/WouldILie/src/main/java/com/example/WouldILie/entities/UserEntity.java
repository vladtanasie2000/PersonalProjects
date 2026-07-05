package com.example.WouldILie.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Table(name = "Users")
public class UserEntity {
  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
  @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
  private Long id;

  @Column(unique = true)
  String name;

  String passwordHash;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  List<QuestionEntity> questions;

  @ManyToOne
  @JoinColumn(name = "team_id")
  TeamEntity team;

  @OneToMany(mappedBy = "user")
  List<VoteEntity> votes;

  private String role = "USER";

  public String getRole() {
    return role;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public List<QuestionEntity> getQuestions() {
    return questions;
  }

  public void setQuestions(List<QuestionEntity> questions) {
    this.questions = questions;
  }

  public TeamEntity getTeam() {
    return team;
  }

  public void setTeam(TeamEntity team) {
    this.team = team;
  }

  public List<VoteEntity> getVotes() {
    return votes;
  }

  public void setVotes(List<VoteEntity> votes) {
    this.votes = votes;
  }

  @Override
  public String toString() {
    return "UserEntity [id=" + id + ", name=" + name + "]";
  }
}
