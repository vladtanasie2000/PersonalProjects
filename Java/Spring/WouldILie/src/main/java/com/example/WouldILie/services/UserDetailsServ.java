package com.example.WouldILie.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.WouldILie.entities.UserEntity;

@Service
public class UserDetailsServ implements UserDetailsService {

  UserEntityRepoServ userEntityRepoServ;

  public UserDetailsServ(UserEntityRepoServ userEntityRepoServ) {
    this.userEntityRepoServ = userEntityRepoServ;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity = userEntityRepoServ.findByName(username);
    return User.builder()
        .username(userEntity.getName())
        .password(userEntity.getPasswordHash())
        .roles(userEntity.getRole())
        .build();
  }
}
