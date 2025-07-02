package com.example.auth.auth.aplication;

import com.example.auth.auth.domain.entities.Users;
import com.example.auth.auth.presentation.dtos.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServices extends UserDetailsService {
  Users registerUser(RegisterRequest request);
  Users getUserByEmail(String email);
}
