package com.example.conduit.dtos;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public final class ProfileResponse {
  private final String username;
  private final String bio;
  private final String image;

  @Setter
  private boolean following = false;
}
