package com.pancisin.bookster.events.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.pancisin.bookster.services.EmailService;
import com.pancisin.bookster.services.NotificationService;
import com.pancisin.bookster.events.OnInviteEvent;

@Component
public class InvitationListener implements ApplicationListener<OnInviteEvent> {

	@Autowired
	private EmailService emailService;

	@Autowired
	private NotificationService notificationService;

	@Override
	public void onApplicationEvent(OnInviteEvent event) {
		if (event.getInvitation().getUser() != null) {
			notificationService.notifyUser(event.getInvitation().getUser(), "notification.event.invitation", event.getInvitation().getEvent().getName());
			emailService.sendSimpleMessage(event.getInvitation().getUser().getEmail(), "Invitation",
					"You've been invited to " + event.getInvitation().getEvent().getName());
		} else {
			emailService.sendSimpleMessage(event.getInvitation().getEmail(), "Invitation",
					"You've been invited to " + event.getInvitation().getEvent().getName());
		}
	}
}
