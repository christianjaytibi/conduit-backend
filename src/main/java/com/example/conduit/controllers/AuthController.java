package com.example.conduit.controllers;

import com.example.conduit.dtos.RegisterUserRequest;
import com.example.conduit.dtos.UserResponse;
import com.example.conduit.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(
  value = "api/users",
  produces = {MediaType.APPLICATION_JSON_VALUE},
  consumes = {MediaType.APPLICATION_JSON_VALUE})
public class AuthController {
  private final UserService service;

  /**
   * Registers a new user
   * @param request The provided user data
   * @return 201 CREATED if success, 422 if failed validation
   * @see RegisterUserRequest
   */
  @PostMapping
  public ResponseEntity<UserResponse> registration(
    @Valid @RequestBody RegisterUserRequest request
    ) {
    var response = service.register(request);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(response);
  }
}
