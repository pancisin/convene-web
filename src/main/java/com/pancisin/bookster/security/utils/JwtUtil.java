package com.pancisin.bookster.security.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pancisin.bookster.models.User;
import com.pancisin.bookster.models.enums.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;

	public User parseToken(String token) {
		try {
			Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

			User u = new User();
			u.setEmail(body.getSubject());
			u.setId(Long.parseLong((String) body.get("userId")));
			u.setRole(Role.valueOf((String) body.get("role")));

			return u;

		} catch (JwtException | ClassCastException e) {
			System.err.println(e.getMessage());
		}

		return null;
	}

	public String generateToken(User u) {
		Claims claims = Jwts.claims().setSubject(u.getUsername());
		claims.put("userId", u.getId() + "");
		claims.put("role", u.getRole().getName());

		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
	}
}
