package com.pancisin.bookster.components;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pancisin.bookster.models.Address;
import com.pancisin.bookster.models.EventBot;
import com.pancisin.bookster.models.EventBotRun;
import com.pancisin.bookster.models.Media;
import com.pancisin.bookster.models.Place;
import com.pancisin.bookster.models.enums.Visibility;
import com.pancisin.bookster.repository.EventBotRepository;
import com.pancisin.bookster.repository.EventBotRunRepository;
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

	@Autowired
	private EventBotRunRepository eventBotRunRepository;

	@Scheduled(cron = "0 0 6 * * *")
	public List<EventBotRun> run() {
		List<EventBot> bots = eventBotRepository.findAll();
		List<EventBotRun> runs = new ArrayList<EventBotRun>();
		
		Facebook fb = new FacebookFactory().getInstance();

		try {
			fb.setOAuthAccessToken(fb.getOAuthAppAccessToken());
		} catch (FacebookException e1) {
			e1.printStackTrace();
		}

		bots.stream().forEach(b -> {
			if (b.isActive()) {
				try {
					runs.add(this.run(b, fb));
				} catch (FacebookException e) {
					e.printStackTrace();
				}
			}
		});

		return runs;
	}

	public EventBotRun run(EventBot bot) {
		Facebook fb = new FacebookFactory().getInstance();

		try {
			fb.setOAuthAccessToken(fb.getOAuthAppAccessToken());
			return this.run(bot, fb);
		} catch (FacebookException e1) {
			e1.printStackTrace();
		}

		return eventBotRunRepository.save(new EventBotRun(bot, false, 0));
	}

	private EventBotRun run(EventBot bot, Facebook fb) throws FacebookException {
		int savedEventsCount = 0;

		ResponseList<Event> events = fb.getEvents(bot.getFbPageId(),
				new Reading().fields("name", "description", "place", "id", "start_time"));

		for (int j = 0; j < events.size(); j++) {
			Event ev = events.get(j);
			com.pancisin.bookster.models.Event event = buildEvent(ev);
			event.setPoster(new Media(fb.getEventPictureURL(ev.getId(), PictureSize.large).toString()));
			event.setPage(bot.getPage());

			if (ev.getVenue() != null) {
				Place place = placeRepository.findByFacebookId(ev.getVenue().getId());
				if (place == null)
					place = placeRepository.save(buildPlace(ev.getLocation(), ev.getVenue()));

				event.setPlace(place);
			}

			try {
				if (eventRepository.save(event) != null) {
					savedEventsCount++;
				}
			} catch (ConstraintViolationException | DataIntegrityViolationException ex) {
				ex.printStackTrace();
			}
		}

		return eventBotRunRepository.save(new EventBotRun(bot, true, savedEventsCount));
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
