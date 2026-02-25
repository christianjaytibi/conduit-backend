package com.example.conduit.configs;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

  @Value("${jwt.secret-key}")
  private String jwtSecret;

  @Bean
  public JwtEncoder encoder() {
    JWK jwk = new OctetSequenceKey.Builder(jwtSecret.getBytes(StandardCharsets.UTF_8))
      .algorithm(JWSAlgorithm.HS256)
      .keyID("conduit-key-id")
      .build();
    JWKSet jwkSet = new JWKSet(jwk);
    JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet);
    return new NimbusJwtEncoder(jwkSource);
  }

  @Bean
  public JwtDecoder decoder() {
    SecretKey secretKey = new SecretKeySpec(
      jwtSecret.getBytes(StandardCharsets.UTF_8),
      "HmacSHA256");

    return NimbusJwtDecoder.withSecretKey(secretKey).build();
  }
}
