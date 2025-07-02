package com.example.auth.auth.aplication.impl;

import com.example.auth.auth.aplication.TokenGeneratorService;
import com.example.auth.auth.aplication.UserServices;
import com.example.auth.auth.domain.entities.Users;
import com.example.auth.auth.domain.enums.TokenType;
import com.example.auth.auth.domain.valueObjects.Token;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenGeneratorServiceImpl implements TokenGeneratorService {

  private final UserServices userServices;
  private final JwtEncoder accessTokenEncoder;
  private final JwtDecoder accessTokenDecoder;

  public TokenGeneratorServiceImpl(UserServices userServices, JwtEncoder accessTokenEncoder, JwtDecoder accessTokenDecoder) {
    this.userServices = userServices;
    this.accessTokenDecoder = accessTokenDecoder;
    this.accessTokenEncoder = accessTokenEncoder;
  }

  @Override
  public Token generateAccessToken(String email, String scopes) {
    Users user = userServices.getUserByEmail(email);

    Instant now = Instant.now();

    long ACCESS_TOKEN_EXPIRATION_TIME = 3600L;
    Instant expiresAt = now.plusSeconds(ACCESS_TOKEN_EXPIRATION_TIME);

    JwtClaimsSet claimsSet = JwtClaimsSet.builder()
        .issuedAt(now)
        .expiresAt(expiresAt)
        .subject(user.getId().toString())
        .claim("email", user.getEmail())
        .claim("name", user.getFullName())
        .claim("scope", scopes)
        .claim("kind", TokenType.ACCESS.getType())
        .build();

    JwsHeader header = JwsHeader.with(() -> "HS256").build();
    return Token.builder()
        .value(accessTokenEncoder.encode(JwtEncoderParameters.from(header, claimsSet)).getTokenValue())
        .tokenType("Bearer")
        .expiresAt(expiresAt.toString())
        .build();
  }
}
