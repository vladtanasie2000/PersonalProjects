package com.example.WouldILie.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.WouldILie.entities.QuestionEntity;
import com.example.WouldILie.entities.UserEntity;
import com.example.WouldILie.entities.VoteEntity;

public interface QuestionEntityRepo extends JpaRepository<QuestionEntity, Long> {
  public Optional<List<QuestionEntity>> findByUser(UserEntity user);

  @Query("select q.isTrue from QuestionEntity q where q.id=:id")
  public Optional<Boolean> isQuestionTrue(@Param("id") Long id);

  @Query("select u.questions from UserEntity u where u=:user")
  public Optional<List<QuestionEntity>> findAllByUser(@Param("user") UserEntity user);

  @Query("select u.questions from UserEntity u left join QuestionEntity q where u=:user and q.isTrue=:isTrue ")
  public Optional<List<QuestionEntity>> findAllByUserIsTrue(@Param("user") UserEntity user,
      @Param("isTrue") Boolean isTrue);

  @Query("select max(q.id) from QuestionEntity q")
  public Long max();

  @Query("select q from QuestionEntity q where q.question=:question")
  public Optional<QuestionEntity> findByQuestion(@Param("question") String name);

  @Query("select q from QuestionEntity q where q.isActive=:active")
  public Optional<QuestionEntity> findByIsActive(@Param("active") Boolean isActive);

  // get all Questions bellonging to a vote
  @Query("""
      select distinct v.qEntity
      from VoteEntity v
      where v in :votes
      """)
  public Optional<List<QuestionEntity>> findAllByVotes(@Param("votes") List<VoteEntity> votes);

}
