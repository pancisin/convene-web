package com.pancisin.bookster.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.pancisin.bookster.model.Message;

public class Conversation {
	
	private User participant;
	private List<Message> recentMessages = new ArrayList<Message>();
	
	public Conversation(User participant, List<Message> recentMessages) {
		this.participant = participant;
		this.recentMessages = recentMessages;
	}
	
	public Conversation(User participant, Message message) {
		this.participant = participant;
		this.recentMessages.add(message);
	}
	
	public Long getLastContact() {
		Optional<Long> optionalTime =  recentMessages.stream().map(x -> x.getCreated().getTimeInMillis()).max(Comparator.comparingLong(x -> x));
		
		if (optionalTime.isPresent()) {
			return optionalTime.get(); 
		}
		
		return null;
	}

	public User getParticipant() {
		return participant;
	}


	public List<Message> getRecentMessages() {
		return recentMessages;
	}
}
