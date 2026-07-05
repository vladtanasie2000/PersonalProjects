package com.example.WouldILie.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "Teams")

public class TeamEntity {
  @Id
  @Column(name = "team_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teams_seq")
  @SequenceGenerator(name = "teams_seq", sequenceName = "teams_seq", allocationSize = 1)
  Long id;

  @Column(unique = true)
  String teamName;

  Integer teamPoints;

  @OneToMany(mappedBy = "team")
  List<UserEntity> teamMembers;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTeamName() {
    return teamName;
  }

  public void setTeamName(String teamName) {
    this.teamName = teamName;
  }

  public Integer getTeamPoints() {
    return teamPoints;
  }

  public void setTeamPoints(Integer teamPoints) {
    this.teamPoints = teamPoints;
  }

  public List<UserEntity> getTeamMembers() {
    return teamMembers;
  }

  public void setTeamMembers(List<UserEntity> teamMembers) {
    this.teamMembers = teamMembers;
  }

}
