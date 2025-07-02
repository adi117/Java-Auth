package com.example.auth.auth.presentation;

import com.example.auth.auth.aplication.UserServices;
import com.example.auth.auth.presentation.dtos.RegisterRequest;
import com.example.auth.common.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/users")
public class UserRestController {

  private final UserServices userServices;

  public UserRestController(UserServices userServices) {
    this.userServices = userServices;
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
    return Response.successfulResponse(
        "successfully create user",
        userServices.registerUser(request)
    );
  }
}
