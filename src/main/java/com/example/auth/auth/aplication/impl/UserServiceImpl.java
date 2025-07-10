package com.example.auth.auth.aplication.impl;

import com.example.auth.auth.aplication.UserServices;
import com.example.auth.auth.domain.entities.Users;
import com.example.auth.auth.domain.enums.UserType;
import com.example.auth.auth.domain.valueObjects.AuthUserDetails;
import com.example.auth.auth.infrastructure.repositories.UserRepository;
import com.example.auth.auth.presentation.dtos.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserServices {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Users registerUser(RegisterRequest request) {

//    make the username become email for db checking
    String emailRequest = request + "@adisain.in";

    Optional<Users> usersOptional = userRepository.findByEmail(emailRequest);

    if (usersOptional.isPresent()) {
      throw new RuntimeException("Already registered user with same email!");
    }

    Users users = request.toUsers();

    users.setPasswordHash(passwordEncoder.encode(request.getPassword()));

    return userRepository.save(users);
  }

  @Override
  @Transactional(readOnly = true)
  public Users getUserByEmail(String email) {
    Optional<Users> usersOptional = userRepository.findByEmail(email);
    return usersOptional.orElse(null);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    Optional<Users> userOptional = userRepository.findByEmail(email);

    AuthUserDetails userDetails = new AuthUserDetails();

    if (userOptional.isPresent()) {
      Users user = userOptional.get();
      userDetails.setName(user.getFullName());
      userDetails.setPassword(user.getPasswordHash());
      userDetails.setRole(user.getRole());
    }

    return userDetails;
  }

  @Override
  @Transactional(readOnly = true)
  public Users getProfile(Integer id) {
    Optional<Users> usersOptional = userRepository.findById(id);
    if (usersOptional.isPresent()) {
      Users user = usersOptional.get();
      user.setPasswordHash(null);
      return user;
    } else {
      throw new UsernameNotFoundException("User not found!");
    }
  }
}
