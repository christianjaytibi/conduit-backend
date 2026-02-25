package com.example.conduit.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http){
    http
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests((auth) ->
        auth
          /* Login and registration
          *  Authentication required to access user settings
          * */
          .requestMatchers(HttpMethod.POST, "/api/users/*").permitAll()
          .requestMatchers("/api/user").authenticated()

          /* Public access to profiles
          *  Authentication required to follow another user
          *  */
          .requestMatchers(HttpMethod.GET, "/api/profiles/*").permitAll()
          .requestMatchers("/api/profiles/**").authenticated()

          /* Public access to read articles
          *  Authentication required to write, favorite, and get feed
          * */
          .requestMatchers(HttpMethod.GET, "/api/articles/feed").authenticated()
          .requestMatchers(HttpMethod.GET, "/api/articles/**").permitAll()
          .requestMatchers("/api/articles/**").authenticated()

          /* Public access to all tags */
          .requestMatchers(HttpMethod.GET, "/api/tags").permitAll()

          /* Allow other requests */
          .anyRequest().permitAll()
      )
      .oauth2ResourceServer(oauth2 ->
        oauth2.jwt(Customizer.withDefaults()))
      .sessionManagement((session) ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      );
    return http.build();
  }
}
