package com.example.conduit.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class TokenService {
  private final JwtEncoder encoder;

  public String generateToken(String subject) {
    Instant now = Instant.now();
    JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
    JwtClaimsSet claims = JwtClaimsSet.builder()
      .issuer("self")
      .subject(subject)
      .issuedAt(now)
      .expiresAt(now.plus(15, ChronoUnit.MINUTES))
      .build();

    var parameters = JwtEncoderParameters.from(header, claims);
    return encoder.encode(parameters).getTokenValue();
  }
}
