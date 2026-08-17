package com.example.MarketPlace.confing;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret:your-very-secret-key-that-is-at-least-32-bytes-long}")
    private String jwtSecret;
    @Value("${jwt.expiration-ms:86400000}") // 24 години
    private long jwtExpirationMs = 86400000;


  public SecretKey getSigningKey(){
      return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(UserDetails userDetails){
      return Jwts.builder()
              .subject(userDetails.getUsername())
              .issuedAt(new Date())
              .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
              .signWith(getSigningKey())
              .compact();
  }

  public String getUsernameFromToken(String token){
      return Jwts.parser()
              .verifyWith(getSigningKey())
              .build()
              .parseSignedClaims(token)
              .getPayload()
              .getSubject();
  }

  public boolean validateToken(String authToken){
      try {
           Jwts.parser()
                  .verifyWith(getSigningKey())
                  .build()
                  .parseSignedClaims(authToken);
           return true;
      } catch (JwtException | IllegalArgumentException e) {
          return false;
      }
  }
}
