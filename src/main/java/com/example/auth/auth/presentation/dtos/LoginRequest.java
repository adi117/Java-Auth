package com.example.auth.auth.presentation.dtos;

import lombok.Data;

@Data
public class LoginRequest {
  private String email;
  private String password;
}
