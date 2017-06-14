package com.pancisin.bookster.components;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pancisin.bookster.models.Address;
import com.pancisin.bookster.models.EventBot;
import com.pancisin.bookster.models.Place;
import com.pancisin.bookster.models.enums.Visibility;
import com.pancisin.bookster.repository.EventBotRepository;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.PlaceRepository;

import facebook4j.Event;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.PictureSize;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.Venue;

@Component
public class EventBotService {

	@Autowired
	private EventBotRepository eventBotRepository;

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private EventRepository eventRepository;

	@Scheduled(cron = "0 0 1 * * *")
	public ResponseList<Event> run() {
		List<EventBot> bots = eventBotRepository.findAll();

		Facebook fb = new FacebookFactory().getInstance();

		try {
			fb.setOAuthAccessToken(fb.getOAuthAppAccessToken());

			for (int i = 0; i < bots.size(); i++) {
				EventBot bot = bots.get(i);

				ResponseList<Event> events = fb.getEvents(bot.getFbPageId(),
						new Reading().fields("name", "description", "place", "id", "start_time"));

				for (int j = 0; j < events.size(); j++) {
					Event ev = events.get(j);
					com.pancisin.bookster.models.Event event = buildEvent(ev);
					event.setBannerUrl(fb.getEventPictureURL(ev.getId(), PictureSize.large).toString());
					event.setPage(bot.getPage());
					event.setOwner(bot.getAlias());

					if (ev.getVenue() != null) {
						Place place = placeRepository.findByFacebookId(ev.getVenue().getId());
						if (place == null)
							place = placeRepository.save(buildPlace(ev.getLocation(), ev.getVenue()));

						event.setPlace(place);
					}

					try {
						eventRepository.save(event);
					} catch (ConstraintViolationException | DataIntegrityViolationException ex) {
						ex.printStackTrace();
					}
				}
			}
		} catch (FacebookException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private com.pancisin.bookster.models.Event buildEvent(Event ev) {
		com.pancisin.bookster.models.Event event = new com.pancisin.bookster.models.Event();
		event.setName(ev.getName());
		event.setSummary(ev.getDescription());

		Calendar starts = Calendar.getInstance();
		starts.setTime(ev.getStartTime());

		event.setDate(starts);
		event.setFacebookId(ev.getId());
		event.setVisibility(Visibility.PUBLIC);

		return event;
	}

	private Place buildPlace(String location, Venue venue) {
		Place place = new Place();
		place.setName(location);

		Address a = new Address();
		a.setCity(venue.getCity());
		a.setLatitude(BigDecimal.valueOf(venue.getLatitude()));
		a.setLongitude(BigDecimal.valueOf(venue.getLongitude()));
		// a.setNumber(number);
		a.setStreet(venue.getStreet());
		a.setZip(venue.getZip());
		a.setState(venue.getState());

		place.setAddress(a);
		return place;
	}
}
