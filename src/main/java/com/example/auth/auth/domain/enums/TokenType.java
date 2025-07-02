package com.example.auth.auth.domain.enums;

public enum TokenType {
  ACCESS("Access"),
  REFRESH("Refresh");

  private final String type;

  TokenType(String type){this.type = type;}

  public String getType(){return type;}
}
