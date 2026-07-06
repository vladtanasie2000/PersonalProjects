package com.example.WouldILie.repos;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.WouldILie.entities.UserEntity;

public interface UserEntityRepo extends JpaRepository<UserEntity, Long> {

  @Query("""
          select coalesce(sum(q.points), 0)
          from VoteEntity v
          join v.qEntity q
          where v.user = :userEntity
            and v.isTrue = q.isTrue
            and q.wasUsed = true
            and q.isActive = false
      """)
  Long findScore(@Param("userEntity") UserEntity userEntity);

  @Query("select u from UserEntity u where u.name=:name")
  public Optional<UserEntity> findByName(@Param("name") String name);

  @Query("select u from UserEntity u join TeamEntity t where t.teamName=:teamName")
  public Optional<List<UserEntity>> findAllByUsername(@Param("teamName") String teamName);

  @Query("select count(u)>0 from UserEntity u where u.name=:name")
  public boolean userExist(String name);

}
