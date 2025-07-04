package com.example.auth.auth.infrastructure.security.filter;

import com.example.auth.auth.aplication.BlacklistTokenService;
import com.example.auth.auth.infrastructure.security.ExtractTokenHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class BlackListTokenFilter extends OncePerRequestFilter {
  private final BlacklistTokenService blacklistToken;

  public BlackListTokenFilter(BlacklistTokenService blacklistToken) {
    this.blacklistToken = blacklistToken;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//    String token = ExtractTokenHelper.getTokenFromRequest(request);
//    if(token != null && blacklistToken.isBlackListed(token)){
//      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//      return;
//    }
    filterChain.doFilter(request, response);
  }
}
