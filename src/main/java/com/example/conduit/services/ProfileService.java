package com.example.conduit.services;

import com.example.conduit.dtos.ProfileResponse;
import com.example.conduit.embeddable.FollowId;
import com.example.conduit.entities.Profile;
import com.example.conduit.exceptions.ApiException;
import com.example.conduit.mappers.ProfileMapper;
import com.example.conduit.repositories.FollowRepository;
import com.example.conduit.repositories.ProfileRepository;
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

  /**
   * Gets a profile.
   * If authenticated, checks if following.
   * @param jwt The principal
   * @param username The username of requested profile
   * @throws ApiException if username does not exist
   * @see ProfileResponse
   */
  @Transactional(readOnly = true)
  public ProfileResponse getProfile(Jwt jwt , String username) {
    Profile profile = profileRepo
      .findByUsername(username)
      .orElseThrow(() -> new ApiException("Profile not found", HttpStatus.NOT_FOUND));
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
