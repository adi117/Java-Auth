package com.example.auth.auth.domain.valueObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
  private String value;
  private String expiresAt;
  @Builder.Default
  private String tokenType = "Bearer";
}
