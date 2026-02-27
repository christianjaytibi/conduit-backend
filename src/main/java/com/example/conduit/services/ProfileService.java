package com.example.conduit.services;

import com.example.conduit.dtos.ProfileResponse;
import com.example.conduit.embeddable.FollowId;
import com.example.conduit.entities.Follow;
import com.example.conduit.entities.Profile;
import com.example.conduit.exceptions.ApiException;
import com.example.conduit.mappers.ProfileMapper;
import com.example.conduit.repositories.FollowRepository;
import com.example.conduit.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
  private final ProfileRepository profileRepo;
  private final ProfileMapper mapper;
  private final FollowRepository followRepo;

  /**
   * Loads a profile from a given username
   * @param username The username of requested profile
   * @throws ApiException if profile is not found
   * @return The profile entity
   */
  private Profile findProfileByUsername(String username) {
    return profileRepo
      .findByUsername(username)
      .orElseThrow(() -> new ApiException("Profile not found", HttpStatus.NOT_FOUND));
  }

  /**
   * Checks if user is following another
   * @param follower The ID of the supposed follower
   * @param following The ID of the supposed followed
   */
  public boolean isFollowing(UUID follower, UUID following) {
    var followId = new FollowId(follower, following);
    return followRepo.existsById(followId);
  }

  /**
   * User-following logic
   * @param currentUserId The ID of the current user
   * @param username The username of requested profile
   * @throws ApiException if following self
   * @see ProfileResponse
   */
  @Transactional
  public ProfileResponse follow(UUID currentUserId, String username) {
    Profile profile = findProfileByUsername(username);
    UUID otherUserId = profile.getUser().getId();

    if (currentUserId.equals(otherUserId))
      throw new ApiException("You cannot follow yourself", HttpStatus.CONFLICT);
    if (isFollowing(currentUserId, otherUserId)) {
      throw new ApiException("You are already following", HttpStatus.CONFLICT);
    }

    Follow follow = new Follow(currentUserId, otherUserId);
    followRepo.save(follow);
    ProfileResponse response = mapper.toDto(profile);
    response.setFollowing(true);
    return response;
  }

  @Transactional
  public ProfileResponse unfollow(UUID currentUserId, String username) {
    Profile profile = findProfileByUsername(username);
    UUID otherUserId = profile.getUser().getId();

    if (isFollowing(currentUserId, otherUserId))
      followRepo.deleteById(new FollowId(currentUserId, otherUserId));

    return mapper.toDto(profile);
  }

  /**
   * Gets a profile.
   * If authenticated, checks if following.
   * @param currentUserId The ID of the current user
   * @param username The username of requested profile
   * @see ProfileResponse
   */
  @Transactional(readOnly = true)
  public ProfileResponse fetchProfile(UUID currentUserId , String username) {
    Profile profile = findProfileByUsername(username);
    var response = mapper.toDto(profile);

    UUID otherUserId = profile.getUser().getId();
    boolean isFollower = isFollowing(currentUserId, otherUserId);
    response.setFollowing(isFollower);
    return response;
  }

  @Transactional
  public ProfileResponse fetchProfile(String username) {
    Profile profile = findProfileByUsername(username);
    return mapper.toDto(profile);
  }
}
