package com.example.conduit.repositories;

import com.example.conduit.entities.Profile;
import com.example.conduit.projections.ProfileView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
  Optional<ProfileView> findByUsername(String username);
}
