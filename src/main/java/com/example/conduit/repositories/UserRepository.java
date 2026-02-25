package com.example.conduit.repositories;

import com.example.conduit.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
  boolean existsByEmailOrProfileUsername(String email, String username);
  Optional<User> findByEmail(String email);
  Optional<User> findByProfileUsername(String username);
}
