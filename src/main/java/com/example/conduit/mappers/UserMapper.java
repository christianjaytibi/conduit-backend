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
  UserResponse toDtoWithToken(User user, String token);
}
