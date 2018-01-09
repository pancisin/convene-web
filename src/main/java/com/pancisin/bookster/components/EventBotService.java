package com.pancisin.bookster.components;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.pancisin.api.facebookapi.api.FacebookApi;
import com.pancisin.api.facebookapi.model.Event;
import com.pancisin.api.facebookapi.model.Paginator;
import com.pancisin.api.facebookapi.utils.Reading;
import com.pancisin.bookster.model.Media;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pancisin.bookster.model.Address;
import com.pancisin.bookster.model.EventBot;
import com.pancisin.bookster.model.EventBotRun;
import com.pancisin.bookster.model.Place;
import com.pancisin.bookster.model.enums.BotRunState;
import com.pancisin.bookster.model.enums.PageState;
import com.pancisin.bookster.model.enums.Visibility;
import com.pancisin.bookster.repository.EventBotRepository;
import com.pancisin.bookster.repository.EventBotRunRepository;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.PlaceRepository;

import facebook4j.PictureSize;
import facebook4j.Venue;
import retrofit2.Call;
import retrofit2.Response;

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

  private final String eventFields = "name,description,place,id,start_time,picture.type(large)";

  @Scheduled(cron = "0 0 6 * * *")
  public List<EventBotRun> run() {
    List<EventBot> bots = eventBotRepository.findAll();
    List<EventBotRun> runs = new ArrayList<EventBotRun>();

    FacebookApi api = FacebookApi.Factory.create();

    bots.forEach(b -> {
      if (b.getActive()) {
        try {
          runs.add(this.run(b, api));
        } catch (IOException ex) {
//          ex.printStackTrace();
        }
      }
    });

    return null;
  }

  public EventBotRun run(EventBot bot) {
    FacebookApi api = FacebookApi.Factory.create();
    try {
      return eventBotRunRepository.save(this.run(bot, api));
    } catch (IOException ex) {
  		EventBotRun failedRun = new EventBotRun(bot, BotRunState.ERROR);
	  	return eventBotRunRepository.save(failedRun);
    }
  }

  private EventBotRun run(EventBot bot, FacebookApi api) throws IOException{
    int savedEventsCount = 0;
    Call call = api.getEvents(bot.getFbPageId(), new Reading().fields(eventFields).since(new Date()));

    Response<Paginator<com.pancisin.api.facebookapi.model.Event>> response = call.execute();

    if (response.isSuccessful()) {
      List<com.pancisin.bookster.model.Event> events = response.body().getData().stream().map(this::buildEvent).collect(Collectors.toList());

      for (com.pancisin.bookster.model.Event e : events) {
        try {
          eventRepository.save(e);
          savedEventsCount++;
        } catch (ConstraintViolationException | DataIntegrityViolationException ex) {
//          ex.printStackTrace();
        }
      }
    }

    EventBotRun run = new EventBotRun(bot, BotRunState.SUCCESS);
    run.setEventsCount(savedEventsCount);
    return run;
  }

  private com.pancisin.bookster.model.Event buildEvent(Event ev) {
    com.pancisin.bookster.model.Event event = new com.pancisin.bookster.model.Event();
    event.setName(ev.getName());
    event.setSummary(ev.getDescription());

    if (ev.getStartTime() != null) {
      Calendar starts = Calendar.getInstance();
      starts.setTime(ev.getStartTime());
      starts.setTimeZone(TimeZone.getTimeZone("UTC"));
      event.setDate(starts);
    }

    event.setFacebookId(ev.getId());
    event.setVisibility(Visibility.PUBLIC);
    event.setPoster(new Media(ev.getPicture().getData().getUrl()));

    if (ev.getPlace() != null && ev.getPlace().getLocation() != null) {
      event.setLatitude(BigDecimal.valueOf(ev.getPlace().getLocation().getLatitude()));
      event.setLongitude(BigDecimal.valueOf(ev.getPlace().getLocation().getLongitude()));
    }

    return event;
  }
}

