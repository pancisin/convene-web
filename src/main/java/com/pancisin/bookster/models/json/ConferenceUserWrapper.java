package com.pancisin.bookster.models.json;

import java.util.List;

import com.pancisin.bookster.models.ConferenceMetaValue;
import com.pancisin.bookster.models.User;

public class ConferenceUserWrapper {

	private User user;

	private List<ConferenceMetaValue> meta;

	public ConferenceUserWrapper(User user, List<ConferenceMetaValue> values) {
		this.user = user;
		this.meta = values;
	}

	public User getUser() {
		return user;
	}

	public List<ConferenceMetaValue> getMeta() {
		return meta;
	}
}
