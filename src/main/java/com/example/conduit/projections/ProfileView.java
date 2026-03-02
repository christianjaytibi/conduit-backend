package com.example.conduit.projections;

import java.util.UUID;

public record ProfileView(
  UUID id,
  String username,
  String bio,
  String image
) {
}
