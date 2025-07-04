package com.example.auth.auth.infrastructure.security;

import com.example.auth.auth.aplication.UserServices;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Log
public class SecurityConfig {
  private final UserServices userServices;
  private final PasswordEncoder passwordEncoder;
  private final JwtConfigProperties jwtConfigProperties;

  public SecurityConfig (UserServices userServices, PasswordEncoder passwordEncoder, JwtConfigProperties jwtConfigProperties){
    this.userServices = userServices;
    this.passwordEncoder = passwordEncoder;
    this.jwtConfigProperties = jwtConfigProperties;
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userServices);
    authenticationProvider.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(authenticationProvider);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(new CorsConfigurationImpl()))
        .authorizeHttpRequests(auth -> auth
//            allow public endpoint
            .requestMatchers("/api/v1/public/**").permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .oauth2ResourceServer(oauth2 -> {
          oauth2.jwt(jwt -> jwt.decoder(jwtDecoder()));
          oauth2.bearerTokenResolver(ExtractTokenHelper::getTokenFromRequest);
        })
        .userDetailsService(userServices)
        .build();
  }

  @Bean(name = "jwtDecoder")
  public JwtDecoder jwtDecoder() {
    SecretKey secretKey = new SecretKeySpec(jwtConfigProperties.getSecret().getBytes(), "HmacSHA256");
    return NimbusJwtDecoder.withSecretKey(secretKey).build();
  }

  @Bean(name = "jwtEncoder")
  public JwtEncoder jwtEncoder() {
    SecretKey secretKey = new SecretKeySpec(jwtConfigProperties.getSecret().getBytes(), "HmacSHA256");
    JWKSource<SecurityContext> immutableSecret = new ImmutableSecret<SecurityContext>(secretKey);
    return new NimbusJwtEncoder(immutableSecret);
  }

  @Bean(name = "refreshTokenDecoder")
  public JwtDecoder refreshTokenDecoder() {
    SecretKey refreshSecretKey = new SecretKeySpec(jwtConfigProperties.getRefreshSecret().getBytes(), "HmacSHA256");
    return NimbusJwtDecoder.withSecretKey(refreshSecretKey).build();
  }

  @Bean(name = "refreshTokenEncoder")
  public JwtEncoder refreshTokenEncoder() {
    SecretKey refreshSecretKey = new SecretKeySpec(jwtConfigProperties.getRefreshSecret().getBytes(), "HmacSHA256");
    JWKSource<SecurityContext> immutableRefreshSecret = new ImmutableSecret<SecurityContext>(refreshSecretKey);
    return new NimbusJwtEncoder(immutableRefreshSecret);
  }
}
