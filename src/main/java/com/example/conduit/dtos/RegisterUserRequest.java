package com.example.conduit.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
  @NotBlank(message = "{error.required.field}")
  @Size(min = 2, message = "{error.size.username}")
  @Pattern(
    regexp = "^[a-zA-Z0-9._]+$",
    message = "{error.pattern.username}")
  String username,

  @NotNull
  @Email(message = "{error.required.field}")
  String email,

  @NotBlank(message = "{error.required.field}")
  @Size(min = 8, message = "{error.size.password}")
  String password
) {
}
