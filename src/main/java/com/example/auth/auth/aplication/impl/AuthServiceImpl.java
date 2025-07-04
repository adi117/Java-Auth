package com.example.auth.auth.aplication.impl;

import com.example.auth.auth.aplication.AuthServices;
import com.example.auth.auth.aplication.TokenGeneratorService;
import com.example.auth.auth.presentation.dtos.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthServices {
  private final AuthenticationManager authenticationManager;
  private final TokenGeneratorService tokenGeneratorService;

  public AuthServiceImpl(AuthenticationManager authenticationManager, TokenGeneratorService tokenGeneratorService){
    this.authenticationManager = authenticationManager;
    this.tokenGeneratorService = tokenGeneratorService;
  }

  @Override
  public LoginResponse credentialLogin(String email, String password){
    try{
      Authentication loginResult = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
      String scope = loginResult.getAuthorities().stream().map(GrantedAuthority::getAuthority).reduce((a, b) -> a + " " + b).orElse("");
      return LoginResponse.builder()
          .accessToken(tokenGeneratorService.generateAccessToken(email, scope))
          .refreshToken(tokenGeneratorService.generateRefreshToken(email))
          .build();
    } catch (Exception e) {
      throw new RuntimeException("Login failed:" + e.getMessage());
    }
  }


}
