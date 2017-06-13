package com.pancisin.bookster.components;

import org.springframework.stereotype.Component;

import facebook4j.Event;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;

@Component
public class EventBotService {

	public ResponseList<Event> fetchPageEvents(String page_id) throws FacebookException {
		Facebook fb = new FacebookFactory().getInstance();

		AccessToken token = fb.getOAuthAppAccessToken();
		fb.setOAuthAccessToken(token);;
		return fb.getEvents(page_id);
	}
}
