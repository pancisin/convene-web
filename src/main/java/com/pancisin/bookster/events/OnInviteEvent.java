package com.pancisin.bookster.events;

import org.springframework.context.ApplicationEvent;

import com.pancisin.bookster.model.Invitation;

public class OnInviteEvent extends ApplicationEvent {

	private Invitation invitation;

	public OnInviteEvent(Invitation invitation) {
		super(invitation);

		this.invitation = invitation;
	}

	public Invitation getInvitation() {
		return invitation;
	}
}
