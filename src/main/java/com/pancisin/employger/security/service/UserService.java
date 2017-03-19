package com.pancisin.employger.security.service;

import com.pancisin.employger.models.User;

public interface UserService {
	User findByEmail(String email);
	void save(User user);
}
