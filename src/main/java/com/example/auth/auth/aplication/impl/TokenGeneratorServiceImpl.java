package com.example.auth.auth.aplication.impl;

import com.example.auth.auth.aplication.TokenGeneratorService;
import com.example.auth.auth.aplication.UserServices;
import com.example.auth.auth.domain.entities.Users;
import com.example.auth.auth.domain.enums.TokenType;
import com.example.auth.auth.domain.valueObjects.Token;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenGeneratorServiceImpl implements TokenGeneratorService {

  private final UserServices userServices;
  private final JwtEncoder accessTokenEncoder;
  private final JwtEncoder refreshTokenEncoder;
  private final JwtDecoder refreshTokenDecoder;

  public TokenGeneratorServiceImpl(UserServices userServices,
                                   @Qualifier("jwtEncoder") JwtEncoder accessTokenEncoder,
                                   @Qualifier("refreshTokenDecoder") JwtDecoder refreshTokenDecoder,
                                   @Qualifier("refreshTokenEncoder") JwtEncoder refreshTokenEncoder) {
    this.userServices = userServices;
    this.refreshTokenDecoder = refreshTokenDecoder;
    this.accessTokenEncoder = accessTokenEncoder;
    this.refreshTokenEncoder = refreshTokenEncoder;
  }

  @Override
  public Token generateAccessToken(String email, String scopes) {
    Users user = userServices.getUserByEmail(email);

    Instant now = Instant.now();

//    set the access token for 1 hour
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

  @Override
  public Token generateAccessToken(String refreshToken) {
    Jwt decodedToken = refreshTokenDecoder.decode(refreshToken);

    if (decodedToken == null){
      throw new IllegalArgumentException("Invalid token!");
    }

//    check if token is expired
    if (decodedToken.getExpiresAt() != null && decodedToken.getExpiresAt().isBefore(Instant.now())) {
      throw new IllegalArgumentException("Token expired!");
    }

    String kind = decodedToken.getClaimAsString("kind");
    Integer userId = Integer.parseInt(decodedToken.getSubject());
    if (!kind.equals(TokenType.REFRESH.getType())) {
      throw new IllegalArgumentException("Invalid token type!");
    }
    Users user  = userServices.getProfile(userId);
    String scopes = user.getRole();
    return generateAccessToken(user.getEmail(), scopes);
  }

  @Override
  public Token generateRefreshToken(String email) {
    Users user = userServices.getUserByEmail(email);

    Instant now = Instant.now();

//    Set refresh token to be 7 days
    long REFRESH_TOKEN_EXPIRATION_TIME = 604800L;
    Instant expiresAt = now.plusSeconds(REFRESH_TOKEN_EXPIRATION_TIME);

    JwtClaimsSet claimsSet = JwtClaimsSet.builder()
        .issuedAt(now)
        .expiresAt(expiresAt)
        .subject(user.getId().toString())
        .claim("kind", TokenType.REFRESH.getType())
        .build();
    JwsHeader header = JwsHeader.with(() -> "HS256").build();
    return Token.builder()
        .value(refreshTokenEncoder.encode(JwtEncoderParameters.from(header, claimsSet)).getTokenValue())
        .tokenType("Bearer")
        .expiresAt(expiresAt.toString())
        .build();
  }

  @Override
  public boolean isRefreshToken(String token) {
    Jwt decodedToken = refreshTokenDecoder.decode(token);
    String kind = decodedToken.getClaimAsString("kind");

    return kind != null && kind.equals(TokenType.REFRESH.getType());
  }
}
