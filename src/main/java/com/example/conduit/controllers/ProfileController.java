package com.example.conduit.controllers;

import com.example.conduit.dtos.ProfileResponse;
import com.example.conduit.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(
  value = "/api/profiles",
  produces = {MediaType.APPLICATION_JSON_VALUE},
  consumes = {MediaType.APPLICATION_JSON_VALUE})
public class ProfileController {
  private final ProfileService service;

  /**
   * Gets a profile.
   * @param username The username of requested profile
   * @param jwt The principal
   * @return 200 OK if success, 404 if not found
   */
  @GetMapping("/{username}")
  public ResponseEntity<ProfileResponse> profile(
    @PathVariable String username,
    @AuthenticationPrincipal Jwt jwt) {
      var response = service.getProfile(jwt, username);
      return ResponseEntity.ok(response);
  }
}
