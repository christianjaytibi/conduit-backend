package com.example.conduit.mappers;

import com.example.conduit.dtos.ProfileResponse;
import com.example.conduit.dtos.RegisterUserRequest;
import com.example.conduit.entities.Profile;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface ProfileMapper {
  Profile entityFrom(RegisterUserRequest request);
  ProfileResponse toDto(Profile profile);
}
