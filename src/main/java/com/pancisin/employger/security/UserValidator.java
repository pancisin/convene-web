package com.pancisin.employger.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.pancisin.employger.models.User;
import com.pancisin.employger.security.service.UserService;

@Component
public class UserValidator implements Validator {
	
	@Autowired
	private UserService userService;

	@Override
	public boolean supports(Class<?> arg0) {
		return User.class.equals(arg0);
	}

	@Override
	public void validate(Object o, Errors errors) {
		User user = (User) o;

		if (userService.findByEmail(user.getEmail()) != null) {
			errors.rejectValue("uid", "Duplicate.user.email");
		}
		
//		if (!admin.getPasswordConfirm().equals(admin.getPassword())) {
//			errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
//		}
	}
}
