package com.example.WouldILie.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.WouldILie.entities.QuestionEntity;
import com.example.WouldILie.entities.TeamEntity;
import com.example.WouldILie.entities.UserEntity;
import com.example.WouldILie.entities.VoteEntity;

public interface VoteEntityRepo extends JpaRepository<VoteEntity, Long> {

  @Query("""
      select v from VoteEntity v
      join UserEntity u
      join TeamEntity t
      where t=:teamEntity
      """)
  public Optional<List<VoteEntity>> findAllByTeam(@Param("teamEntity") TeamEntity teamEntity);

  @Query("""
      select v from VoteEntity v
      join UserEntity u
      where u=:userEntity
      """)
  public Optional<List<VoteEntity>> findAllByUser(@Param("userEntity") UserEntity UserEntity);

  @Query("""
      select count(v) > 0 from VoteEntity v
      where v.qEntity = :question
        and v.user = :user      """)
  public Optional<Boolean> getVotedOnce(@Param("question") QuestionEntity question,
      @Param("user") UserEntity user);

  @Query("""
        select q from QuestionEntity q, VoteEntity v
        where v member of q.vote
        and q=:question
      """)
  public Optional<List<VoteEntity>> findAllByQuestion(@Param("question") QuestionEntity qEntity);
}
