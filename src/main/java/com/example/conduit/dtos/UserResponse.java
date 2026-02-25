package com.example.conduit.dtos;

public record UserResponse(
  String email,
  String token,
  String username,
  String bio,
  String image
) {
}
