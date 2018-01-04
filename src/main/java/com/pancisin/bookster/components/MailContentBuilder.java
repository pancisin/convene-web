package com.pancisin.bookster.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.pancisin.bookster.model.User;
import com.pancisin.bookster.model.UserSubscription;

@Service
public class MailContentBuilder {

	private TemplateEngine templateEngine;

	@Autowired
	public MailContentBuilder(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	public String build(String header, String content) {
		Context context = new Context();
		context.setVariable("header", header);
		context.setVariable("content", content);
		return templateEngine.process("mailTemplate", context);
	}

	public String buildInvoice(UserSubscription us) {
		Context context = new Context();
		context.setVariable("us", us);
		return templateEngine.process("invoiceEmailTemplate", context);
	}

	public String buildVerification(User user, String url) {
		Context context = new Context();
		context.setVariable("user", user);
		context.setVariable("url", url);
		return templateEngine.process("verificationEmailTemplate", context);
	}
}
