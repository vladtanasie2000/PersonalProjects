package com.example.WouldILie.restcont;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.WouldILie.dtos.users.AddUserDTO;
import com.example.WouldILie.dtos.users.LoginRequestDTO;
import com.example.WouldILie.dtos.users.LoginResponseDTO;
import com.example.WouldILie.entities.UserEntity;
import com.example.WouldILie.services.JwtService;
import com.example.WouldILie.services.UserEntityRepoServ;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserEntityRepoServ userEntityRepoServ;
  private final PasswordEncoder passwordEncoder;

  public AuthRestController(
      AuthenticationManager authenticationManager,
      JwtService jwtService,
      UserEntityRepoServ userEntityRepoServ,
      PasswordEncoder passwordEncoder) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.userEntityRepoServ = userEntityRepoServ;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/login")
  public LoginResponseDTO login(@RequestBody LoginRequestDTO dto) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(dto.name(), dto.plainPass()));

    String token = jwtService.generateToken(authentication.getName());

    return new LoginResponseDTO(token);
  }

  @PostMapping("/register")
  public String addUser(@RequestBody AddUserDTO addUserDTO) {
    // check for existing user
    if (userEntityRepoServ.userExists(addUserDTO.name())) {
      return "Name not unique";
    }
    UserEntity userEntity = new UserEntity();
    userEntity.setName(addUserDTO.name());
    userEntity.setPasswordHash(passwordEncoder.encode(addUserDTO.plainpass()));
    userEntityRepoServ.save(userEntity);
    return "User has been save";
  }
}
