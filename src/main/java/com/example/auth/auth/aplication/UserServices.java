package com.example.auth.auth.aplication;

import com.example.auth.auth.domain.entities.Users;
import com.example.auth.auth.presentation.dtos.RegisterRequest;
import com.example.auth.auth.presentation.dtos.RegisterResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServices extends UserDetailsService {
  RegisterResponse registerUser(RegisterRequest request);
  Users getUserByEmail(String email);
  Users getProfile(Integer id);
}
