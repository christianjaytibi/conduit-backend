package com.example.conduit.dtos;

public record ProfileResponse(
  String username,
  String bio,
  String image,
  boolean following) {
}
