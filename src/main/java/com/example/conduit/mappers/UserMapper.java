package com.example.conduit.mappers;

import com.example.conduit.dtos.RegisterUserRequest;
import com.example.conduit.dtos.UserResponse;
import com.example.conduit.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface UserMapper {

  @Mapping(target = "password", source = "encodedPassword")
  User entityFrom(RegisterUserRequest request, String encodedPassword);

  @Mapping(target = "username", source = "profile.username")
  @Mapping(target = "bio", source = "profile.bio")
  @Mapping(target = "image", source = "profile.image")
  UserResponse toDto(User user);

  @Mapping(target = "username", source = "user.profile.username")
  @Mapping(target = "bio", source = "user.profile.bio")
  @Mapping(target = "image", source = "user.profile.image")
  UserResponse toDtoWithToken(User user, String token);
}
