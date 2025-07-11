package com.example.auth.auth.presentation.dtos;

import com.example.auth.auth.domain.entities.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
  private String name;
  private String username;
  private String email;
  private Integer userID;

  public static RegisterResponse toResponse(Users users) {
    return RegisterResponse.builder()
        .email(users.getEmail())
        .name(users.getFullName())
        .username(users.getEmail().replace("@adisain.in", "")) //remove email domain to get the username
        .userID(users.getId())
        .build();
  }
}
