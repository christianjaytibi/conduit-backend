package com.example.conduit.mappers;

import com.example.conduit.dtos.ProfileResponse;
import com.example.conduit.dtos.RegisterUserRequest;
import com.example.conduit.entities.Profile;
import com.example.conduit.projections.ProfileView;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface ProfileMapper {
  Profile mapToEntity(RegisterUserRequest request);
  ProfileResponse mapToDto(ProfileView projection, boolean following);
}
