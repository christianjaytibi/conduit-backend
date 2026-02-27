package com.example.conduit.services;

import com.example.conduit.dtos.ProfileResponse;
import com.example.conduit.embeddable.FollowId;
import com.example.conduit.entities.Follow;
import com.example.conduit.entities.Profile;
import com.example.conduit.exceptions.ApiException;
import com.example.conduit.mappers.ProfileMapper;
import com.example.conduit.repositories.FollowRepository;
import com.example.conduit.repositories.ProfileRepository;
import com.example.conduit.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
  private final ProfileRepository profileRepo;
  private final ProfileMapper mapper;
  private final FollowRepository followRepo;
  private final UserRepository userRepo;

  /**
   * Loads a profile from a given username
   * @param username The username of requested profile
   * @throws ApiException if profile is not found
   * @return The profile entity
   */
  private Profile getProfileByUsername(String username) {
    return profileRepo
      .findByUsername(username)
      .orElseThrow(() -> new ApiException("Profile not found", HttpStatus.NOT_FOUND));
  }

  /**
   * User-following logic
   * @param jwt The principal
   * @param username The username of requested profile
   * @throws ApiException if following self
   * @see ProfileResponse
   */
  @Transactional
  public ProfileResponse follow(Jwt jwt, String username) {
    Profile profile = getProfileByUsername(username);
    UUID currentUserId = UUID.fromString(jwt.getSubject());
    UUID otherUserId = profile.getUser().getId();

    if (currentUserId.equals(otherUserId))
      throw new ApiException("You cannot follow yourself", HttpStatus.CONFLICT);

    var followId = new FollowId(currentUserId, otherUserId);
    if (followRepo.existsById(followId)) {
      throw new ApiException("You are already following", HttpStatus.CONFLICT);
    }

    Follow follow = new Follow(followId);
    followRepo.save(follow);
    ProfileResponse response = mapper.toDto(profile);
    response.setFollowing(true);
    return response;
  }

  /**
   * Gets a profile.
   * If authenticated, checks if following.
   * @param jwt The principal
   * @param username The username of requested profile
   * @see ProfileResponse
   */
  @Transactional(readOnly = true)
  public ProfileResponse getProfile(Jwt jwt , String username) {
    Profile profile = getProfileByUsername(username);
    ProfileResponse response = mapper.toDto(profile);
    if (jwt == null)
      return response;

    UUID currentUserId = UUID.fromString(jwt.getSubject());
    UUID otherUserId = profile.getUser().getId();
    boolean isFollowing = followRepo.existsById(new FollowId(currentUserId, otherUserId));
    response.setFollowing(isFollowing);
    return response;
  }
}
