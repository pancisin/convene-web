package com.pancisin.employger.rest.controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.employger.models.User;
import com.pancisin.employger.repository.UserRepository;
import com.pancisin.employger.rest.controllers.exceptions.InvalidRequestException;
import com.pancisin.employger.security.models.JwtAuthenticationToken;

@RestController
public class AuthController {

	@Value("${jwt.secret}")
	private String secret;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = { "/api/user", "/api/me" }, method = RequestMethod.GET)
	public ResponseEntity<?> user(Principal principal) {
		User auth_user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userRepository.findOne(auth_user.getId());
		return ResponseEntity.ok(user);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody User user) {
		User stored = userRepository.findByEmail(user.getEmail());
		user.setHashedPassword(hashPassword(user.getPassword()));
		
		if (user == null || !stored.getHashedPassword().equals(user.getHashedPassword())) {
			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		}

		return ResponseEntity.ok(JwtAuthenticationToken.generateToken(stored, secret));
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			throw new InvalidRequestException("Invalid data", bindingResult);

		if (userRepository.findByEmail(user.getEmail()) != null) {
			bindingResult.rejectValue("email", "Duplicate.user.email");
			throw new InvalidRequestException("Invalid data", bindingResult);
		} else {
			user.setLocked(false);
			user.setHashedPassword(hashPassword(user.getPassword()));
			userRepository.save(user);
			return ResponseEntity.ok(JwtAuthenticationToken.generateToken(user, secret));
		}
	}

	private String hashPassword(String password) {
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}
}
