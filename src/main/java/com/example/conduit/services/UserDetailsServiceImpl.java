package com.example.conduit.services;

import com.example.conduit.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository repo;

  @Override
  public @NullMarked UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return repo.findByEmail(email)
      .map(user -> User.builder()
        .username(user.getEmail())
        .password(user.getPassword())
        .build())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}
