package com.example.WouldILie.services;

import java.time.Instant;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import com.example.WouldILie.entities.UserEntity;

@Service
public class JwtService {
  private final JwtEncoder jwtEncoder;
  private final UserEntityRepoServ userEntityRepoServ;

  public JwtService(JwtEncoder jwtEncoder, UserEntityRepoServ userEntityRepoServ) {
    this.jwtEncoder = jwtEncoder;
    this.userEntityRepoServ = userEntityRepoServ;

  }

  public String generateToken(String username) {
    Instant instant = Instant.now();
    UserEntity user = userEntityRepoServ.findByName(username);
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer("wouldILie")
        .issuedAt(instant)
        .expiresAt(instant.plusSeconds(60 * 60))
        .subject(user.getName())
        .claim("role", user.getRole())
        .build();
    JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
    return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

  }

}
