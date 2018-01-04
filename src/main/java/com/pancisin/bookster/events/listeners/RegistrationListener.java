package com.pancisin.bookster.events.listeners;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.pancisin.bookster.components.EmailService;
import com.pancisin.bookster.events.OnRegistrationCompleteEvent;
import com.pancisin.bookster.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	@Autowired
	private EmailService emailService;

	@Value("${jwt.verificationSecret}")
	private String verificationSecret;

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		User user = event.getUser();

		Claims claims = Jwts.claims().setSubject(user.getUsername());
		claims.put("userId", user.getId() + "");
		claims.setExpiration(calculateExpiration());

		String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, verificationSecret).compact();
		String confirmationUrl = event.getAppUrl() + "/#/confirm-email?token=" + token;

		emailService.sendVerificationEmail(user, confirmationUrl);
	}

	private Date calculateExpiration() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, 12);
		return cal.getTime();
	}
}
