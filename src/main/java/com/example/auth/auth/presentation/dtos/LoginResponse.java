package com.example.auth.auth.presentation.dtos;

import com.example.auth.auth.domain.valueObjects.Token;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
  private Token accessToken;
  private Token refreshToken;
}
