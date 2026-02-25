package com.example.conduit.services;

import com.example.conduit.dtos.RegisterUserRequest;
import com.example.conduit.dtos.UserResponse;
import com.example.conduit.entities.Profile;
import com.example.conduit.entities.User;
import com.example.conduit.exceptions.ApiException;
import com.example.conduit.mappers.ProfileMapper;
import com.example.conduit.mappers.UserMapper;
import com.example.conduit.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository repo;
  private final UserMapper userMapper;
  private final ProfileMapper profileMapper;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;

  /**
   * Registers a new user
   * @param request Contains information about the registrant
   * @throws ApiException if the email or username is unprocessable
   * @see RegisterUserRequest
   */
  @Transactional
  public UserResponse register(RegisterUserRequest request) {
    if (userAlreadyExists(request.email(), request.username())) {
        throw new ApiException(
          "Email or username is unprocessable",
          HttpStatus.UNPROCESSABLE_CONTENT);
    }
    User user = createNewUser(request);
    String token = tokenService.generateToken(user.getId().toString());
    return userMapper.toDtoWithToken(user, token);
  }

  /**
   * Creates a new user from a given request
   * @param request The required data about a user
   * @return A new user entity
   * @see RegisterUserRequest
   */
  private User createNewUser(RegisterUserRequest request) {
    Profile profile = profileMapper.entityFrom(request);
    String encodedPassword = passwordEncoder.encode(request.password());
    User user = userMapper.entityFrom(request, encodedPassword);
    user.setProfile(profile);

    return repo.save(user);
  }

  /**
   * Checks if a user exists from the given email and username
   * @param email The given email
   * @param username The given username
   * @return True, if any of the two params already exist. Otherwise, false.
   */
  private boolean userAlreadyExists(String email, String username) {
    return repo.existsByEmailOrProfileUsername(email, username);
  }
}
