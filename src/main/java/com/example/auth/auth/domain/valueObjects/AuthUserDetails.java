package com.example.auth.auth.domain.valueObjects;

import com.example.auth.auth.domain.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDetails implements UserDetails {
  private String name;
  private String password;
  private UserType type;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + getType()));
  }

  @Override
  public String getPassword() {return this.password;}

  @Override
  public String getUsername() {return this.name;}
}
