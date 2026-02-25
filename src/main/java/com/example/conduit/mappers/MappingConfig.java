package com.example.conduit.mappers;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
  /* Ignore target fields that the source doesn't have */
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  componentModel = "spring")
public interface MappingConfig {
}
