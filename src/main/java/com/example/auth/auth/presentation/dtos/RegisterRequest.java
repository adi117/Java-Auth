package com.example.auth.auth.presentation.dtos;

import com.example.auth.auth.domain.entities.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
  private String name;
  private String username;
  private String password;

  public Users toUsers() {
    return Users.builder()
        .fullName(name)
        .email(username + "@adisain.in")
        .build();
  }
}
