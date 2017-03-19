package com.pancisin.employger.rest.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.employger.models.User;
import com.pancisin.employger.rest.controllers.exceptions.InvalidRequestException;
import com.pancisin.employger.security.UserValidator;
import com.pancisin.employger.security.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	@Autowired 
	private UserValidator userValidator;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) 
			throw new InvalidRequestException("Invalid data", bindingResult);

		if (userService.findByEmail(user.getEmail()) != null) {
			bindingResult.rejectValue("email", "Duplicate.user.email");
			throw new InvalidRequestException("Invalid data", bindingResult);
		}

		userService.save(user);
		return ResponseEntity.ok("success");
	}
}
