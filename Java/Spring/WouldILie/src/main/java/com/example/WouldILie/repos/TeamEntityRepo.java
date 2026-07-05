package com.example.WouldILie.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.WouldILie.entities.QuestionEntity;
import com.example.WouldILie.entities.TeamEntity;
import com.example.WouldILie.entities.VoteEntity;

@Repository
public interface TeamEntityRepo extends JpaRepository<TeamEntity, Long> {

  @Query("select t from TeamEntity t where t.teamName = :name")
  public Optional<TeamEntity> findByTeamName(@Param("name") String name);

  @Query("""
      select v from VoteEntity v, UserEntity u, TeamEntity t
        where u member of t.teamMembers
        and v member of u.votes
        and t=:teamEntity
      """)
  public Optional<List<VoteEntity>> findVotes(@Param("teamEntity") TeamEntity teamEntity);

  @Query("""
      select q.id,q.isTrue,q.points,count(v),sum(case when v.isTrue = true then 1 else 0 end)
      from TeamEntity t join t.teamMembers u
      join u.votes v
      join v.qEntity q
      where t=:teamEntity
        and q.wasUsed=true
        and q.isActive=false
      group by q.id,q.isTrue,q.points
      """)
  List<Object[]> findStats(@Param("teamEntity") TeamEntity teamEntity);

  @Query("""
      select t from TeamEntity t , QuestionEntity q, UserEntity u
      where q =:question
        and t <> q.user.team
      """)
  public Optional<TeamEntity> findOpposingTeamByQuestion(@Param("question") QuestionEntity question);
}
