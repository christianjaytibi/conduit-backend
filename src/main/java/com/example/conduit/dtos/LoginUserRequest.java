package com.example.conduit.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginUserRequest(
  @NotNull
  @Email(message = "{error.required.field}")
  String email,

  @NotBlank(message = "{error.blank.field}")
  @Size(message = "{error.min.size.string}", min = 2)
  String password
) {
}
