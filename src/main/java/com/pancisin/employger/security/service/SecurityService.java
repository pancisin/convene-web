package com.pancisin.employger.security.service;

public interface SecurityService {
	String findLoggedInEmail();
	void autologin(String email, String password);
}
