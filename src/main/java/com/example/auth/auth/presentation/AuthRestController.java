package com.example.auth.auth.presentation;

import com.example.auth.auth.aplication.AuthServices;
import com.example.auth.auth.presentation.dtos.LoginRequest;
import com.example.auth.common.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/auth")
public class AuthRestController {

  private final AuthServices authServices;

  public AuthRestController(AuthServices authServices) {
    this.authServices = authServices;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    return Response.successfulResponse(
        "Login success!",
        authServices.credentialLogin(request.getEmail(), request.getPassword())
    );
  }
}
