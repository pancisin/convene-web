package com.pancisin.bookster.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

	@Value("${mailing.enabled}")
	private boolean enabled;

	@Autowired
	private JavaMailSender sender;

	public void sendSimpleMessage(String to, String subject, String content) {
		if (enabled) {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(to);
			message.setSubject(subject);
			message.setText(content);
			sender.send(message);
		}
	}
}
