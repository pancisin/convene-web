package com.pancisin.bookster.security.evaluators;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.pancisin.bookster.models.BookRequest;
import com.pancisin.bookster.models.Conference;
import com.pancisin.bookster.models.Event;
import com.pancisin.bookster.models.Notification;
import com.pancisin.bookster.models.Programme;
import com.pancisin.bookster.models.Service;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.models.enums.Visibility;
import com.pancisin.bookster.repository.BookRequestRepository;
import com.pancisin.bookster.repository.ConferenceRepository;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.NotificationRepository;
import com.pancisin.bookster.repository.ProgrammeRepository;
import com.pancisin.bookster.repository.ServiceRepository;
import com.pancisin.bookster.repository.UserRepository;

public class CustomPermissionEvaluator implements PermissionEvaluator {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private ConferenceRepository conferenceRepository;

	@Autowired
	private ProgrammeRepository programmeRepository;

	@Autowired
	private BookRequestRepository bookRequestRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private ServiceRepository serviceRepository;

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		System.out.println("test");
		return true;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {

		User user = (User) authentication.getPrincipal();
		final User stored = userRepository.findOne(user.getId());

		switch (targetType) {
		case "book_request":
			BookRequest bookRequest = bookRequestRepository.findOne((Long) targetId);
			return bookRequest.getService().getPage().getAdministrators().stream()
					.anyMatch(x -> x.getId() == stored.getId());
		case "conference":
			Conference conference = conferenceRepository.findOne((Long) targetId);

			if (permission.equals("update"))
				return stored != null && conference.getOwner().getId() == stored.getId();
			else
				return conference.getVisibility() == Visibility.PUBLIC;
		case "event":
			Event event = eventRepository.findOne((Long) targetId);

			if (permission.equals("update"))
				return stored != null && event.getOwner().getId() == stored.getId();
			else
				return event.getVisibility() == Visibility.PUBLIC;
		case "license":
			return true;
		case "notification":
			Notification notification = notificationRepository.findOne((Long) targetId);
			return notification.getRecipient().getId() == stored.getId();
		case "page":
			if (permission.equals("update"))
				return stored.getPages().stream().anyMatch(p -> p.getId() == targetId);
			else
				return true;
		case "programme":
			Programme programme = programmeRepository.findOne((Long) targetId);
			return programme.getEvent().getOwner().getId() == stored.getId();
		case "service":
			Service service = serviceRepository.findOne((Long) targetId);

			if (permission == "update")
				return service.getPage().getAdministrators().stream().anyMatch(x -> x.getId() == stored.getId());
			else
				return true;
		}

		return false;
	}
}
