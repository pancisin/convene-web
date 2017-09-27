package com.pancisin.bookster.security.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
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

			String[] roleArray = new String[1];
			roleArray[0] = (String) body.get("role");
			
			List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(roleArray);
			
			User user = new User(Long.parseLong((String) body.get("userId")), body.getSubject(), token, authorityList);
      user.setRole(Role.valueOf((String) body.get("role")));
			
			return user;
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
