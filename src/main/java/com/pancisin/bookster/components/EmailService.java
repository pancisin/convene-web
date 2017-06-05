package com.pancisin.bookster.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import com.pancisin.bookster.models.User;
import com.pancisin.bookster.models.UserSubscription;

@Component
public class EmailService {

	@Value("${mailing.enabled}")
	private boolean enabled;

	@Autowired
	private JavaMailSender sender;

	@Autowired
	private MailContentBuilder builder;

	public void sendSimpleMessage(String to, String subject, String content) {
		if (enabled) {
			MimeMessagePreparator messagePreparator = mimeMessage -> {
				MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
				messageHelper.setFrom("Bookster");
				messageHelper.setTo(to);
				messageHelper.setSubject(subject);

				String email = builder.build(subject, content);
				messageHelper.setText(email, true);
			};

			try {
				sender.send(messagePreparator);
			} catch (MailException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	public void sendInvoiceEmail(UserSubscription us) {
		if (enabled) {
			MimeMessagePreparator messagePreparator = mimeMessage -> {
				MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
				messageHelper.setFrom("Bookster");
				messageHelper.setTo(us.getUser().getEmail());
				messageHelper.setSubject("Invoice");

				String email = builder.buildInvoice(us);
				messageHelper.setText(email, true);
			};

			try {
				sender.send(messagePreparator);
			} catch (MailException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	public void sendVerificationEmail(User user, String url) {
		if (enabled) {
			MimeMessagePreparator messagePreparator = mimeMessage -> {
				MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
				messageHelper.setFrom("Bookster");
				messageHelper.setTo(user.getEmail());
				messageHelper.setSubject("Email verification");

				String email = builder.buildVerification(user, url);
				messageHelper.setText(email, true);
			};

			try {
				sender.send(messagePreparator);
			} catch (MailException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	public void sendWelcomeEmail(String to) {

	}
}
