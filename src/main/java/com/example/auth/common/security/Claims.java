package com.example.auth.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Map;

public class Claims {
  public static Map<String, Object> getClaims() {
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication auth = context.getAuthentication();

    if (auth == null || !(auth.getPrincipal() instanceof Jwt)){
      throw new IllegalStateException("No JWT found in SecurityContext");
    }

    return ((Jwt) auth.getPrincipal()).getClaims();
  }

  public static String getTokenValue() {
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication auth = context.getAuthentication();

    if(auth instanceof JwtAuthenticationToken jwtAuthenticationToken){
      Jwt jwt = jwtAuthenticationToken.getToken();
      return jwt.getTokenValue();
    }

    return null;
  }

  public static Integer getUserID() {
    Map<String, Object> claims = getClaims();
    return Integer.parseInt(String.valueOf(claims.get("sub")));
  }
}
