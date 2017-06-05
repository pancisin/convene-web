package com.pancisin.bookster.rest.controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.pancisin.bookster.events.OnRegistrationCompleteEvent;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.repository.LocaleRepository;
import com.pancisin.bookster.repository.UserRepository;
import com.pancisin.bookster.rest.controllers.exceptions.InvalidRequestException;
import com.pancisin.bookster.security.models.JwtAuthenticationToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@RestController
public class AuthController {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.verificationSecret}")
	private String verificationSecret;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LocaleRepository localeRepository;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

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
	public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult bindingResult,
			HttpServletRequest request) {
		if (bindingResult.hasErrors())
			throw new InvalidRequestException("Invalid data", bindingResult);

		Locale locale = LocaleContextHolder.getLocale();
		com.pancisin.bookster.models.Locale l = localeRepository.findByCode(locale.toLanguageTag());

		if (l != null)
			user.setLocale(l);
		else
			user.setLocale(localeRepository.findByCode("en"));

		if (userRepository.findByEmail(user.getEmail()) != null) {
			bindingResult.rejectValue("email", "Duplicate.user.email");
			throw new InvalidRequestException("Invalid data", bindingResult);
		} else {
			user.setLocked(false);
			user.setHashedPassword(hashPassword(user.getPassword()));
			User stored = userRepository.save(user);
			stored.setToken(JwtAuthenticationToken.generateToken(user, secret));

			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(stored, request.getLocale(),
					request.getScheme() + "://" + request.getServerName()));

			return ResponseEntity.ok(stored);
		}
	}

	@PutMapping(name = "/verify")
	private ResponseEntity<?> verifyEmail(@RequestBody String token) {
		User user = null;
		try {
			Claims body = Jwts.parser().setSigningKey(verificationSecret).parseClaimsJws(token).getBody();

			user = userRepository.findOne(Long.parseLong((String) body.get("userId")));
			user.setVerified(true);
			userRepository.save(user);
		} catch (JwtException | ClassCastException e) {
			System.err.println(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		return ResponseEntity.ok(user);
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
