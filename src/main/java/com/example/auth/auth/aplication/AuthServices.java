package com.example.auth.auth.aplication;

import com.example.auth.auth.presentation.dtos.LoginResponse;

public interface AuthServices {
  LoginResponse credentialLogin(String email, String password);
}
