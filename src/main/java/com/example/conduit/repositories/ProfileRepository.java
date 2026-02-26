package com.example.conduit.repositories;

import com.example.conduit.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
  Optional<Profile> findByUsername(String username);
}
