package com.pancisin.employger.security.models;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.pancisin.employger.models.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

  private String token;

  public JwtAuthenticationToken(String token) {
    super(null, null);
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return "principal test";
  }

  public static String generateToken(User u, String secret) {
    Claims claims = Jwts.claims().setSubject(u.getUsername());
    claims.put("userId", u.getId() + "");
//    claims.put("role", u.getRole());
    return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
  }
}
