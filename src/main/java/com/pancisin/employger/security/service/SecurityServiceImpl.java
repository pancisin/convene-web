package com.pancisin.employger.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.pancisin.employger.models.User;
import com.pancisin.employger.repository.UserRepository;

@Service
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

	@Override
	public void autologin(String email, String password) {
		User user = userRepository.findByEmail(email);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				user, password, user.getAuthorities());

		authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		if (usernamePasswordAuthenticationToken.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			logger.debug(String.format("Auto login %s successfully!", email));
		}
	}

	@Override
	public String findLoggedInEmail() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
		return user.getUsername();
	}
}
