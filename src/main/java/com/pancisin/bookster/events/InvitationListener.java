package com.pancisin.bookster.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.pancisin.bookster.components.EmailService;
import com.pancisin.bookster.components.Notifier;

@Component
public class InvitationListener implements ApplicationListener<OnInviteEvent> {

	@Autowired
	private EmailService emailService;

	@Autowired
	private Notifier notifier;

	@Override
	public void onApplicationEvent(OnInviteEvent event) {
		if (event.getInvitation().getUser() != null) {
			notifier.notifyUser(event.getInvitation().getUser(), "Invitation",
					"You've been invited to " + event.getInvitation().getEvent().getName());
			emailService.sendSimpleMessage(event.getInvitation().getUser().getEmail(), "Invitation",
					"You've been invited to " + event.getInvitation().getEvent().getName());
		} else {
			emailService.sendSimpleMessage(event.getInvitation().getEmail(), "Invitation",
					"You've been invited to " + event.getInvitation().getEvent().getName());
		}
	}
}
