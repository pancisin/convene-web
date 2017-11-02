package com.pancisin.bookster.websocket.services;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class UserStats {
	private AtomicLong lastAccess = new AtomicLong(System.currentTimeMillis());
	private Set<String> contacts;
	private String email;
	
	public UserStats(String email, Set<String> contacts) {
		this.contacts = contacts;
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}

	public Set<String> getContacts() {
		return contacts;
	}

	public void mark() {
		lastAccess.set(System.currentTimeMillis());
	}

	public long lastAccess() {
		return lastAccess.get();
	}
}
