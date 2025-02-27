package com.pancisin.bookster.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.pancisin.bookster.model.User;
import com.pancisin.bookster.exceptions.JwtTokenMalformedException;
import com.pancisin.bookster.security.models.JwtAuthenticationToken;
import com.pancisin.bookster.security.utils.JwtUtil;

@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public boolean supports(Class<?> authentication) {
		return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
		String token = jwtAuthenticationToken.getToken();
		User parsedUser = jwtUtil.parseToken(token);
		if (parsedUser == null) {
			throw new JwtTokenMalformedException("JWT token is not valid");
		}

		String[] roleArray = new String[1];
		roleArray[0] = parsedUser.getRole().getProp();

		List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(roleArray);
		return new User(parsedUser.getId(), parsedUser.getEmail(), token, authorityList);
	}
}
