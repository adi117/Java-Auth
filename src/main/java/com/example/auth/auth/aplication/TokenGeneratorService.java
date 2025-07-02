package com.example.auth.auth.aplication;

import com.example.auth.auth.domain.valueObjects.Token;

public interface TokenGeneratorService {
  Token generateAccessToken(String email, String scopes);
//  Token generateAccessToken(String refreshToken);
//  Token generateRefreshToken(String email);
//  boolean isRefreshToken(String token);
}
