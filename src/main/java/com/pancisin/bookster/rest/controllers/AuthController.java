package com.pancisin.bookster.rest.controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.User;
import com.pancisin.bookster.repository.UserRepository;
import com.pancisin.bookster.rest.controllers.exceptions.InvalidRequestException;
import com.pancisin.bookster.security.models.JwtAuthenticationToken;

@RestController
public class AuthController {

	@Value("${jwt.secret}")
	private String secret;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody User user) {
		User stored = userRepository.findByEmail(user.getEmail());
		user.setHashedPassword(hashPassword(user.getPassword()));
		
		if (user == null || stored == null || !stored.getHashedPassword().equals(user.getHashedPassword())) {
			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		}

		stored.setToken(JwtAuthenticationToken.generateToken(stored, secret));
		return ResponseEntity.ok(stored);
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
			User stored = userRepository.save(user);
			stored.setToken(JwtAuthenticationToken.generateToken(user, secret));
			return ResponseEntity.ok(stored);
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
