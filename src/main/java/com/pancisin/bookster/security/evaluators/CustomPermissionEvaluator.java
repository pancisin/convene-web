package com.pancisin.bookster.security.evaluators;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.pancisin.bookster.models.BookRequest;
import com.pancisin.bookster.models.Conference;
import com.pancisin.bookster.models.Event;
import com.pancisin.bookster.models.Notification;
import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.PageAdministrator;
import com.pancisin.bookster.models.Programme;
import com.pancisin.bookster.models.Service;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.models.enums.PageRole;
import com.pancisin.bookster.models.enums.Role;
import com.pancisin.bookster.models.enums.Visibility;
import com.pancisin.bookster.repository.BookRequestRepository;
import com.pancisin.bookster.repository.ConferenceRepository;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.NotificationRepository;
import com.pancisin.bookster.repository.PageAdministratorRepository;
import com.pancisin.bookster.repository.PageRepository;
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
	private PageRepository pageRepository;

	@Autowired
	private ServiceRepository serviceRepository;

	@Autowired
	private PageAdministratorRepository paRepository;
	
	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
//		String targetType = (String) targetDomainObject;
//
//		User user = (User) authentication.getPrincipal();
//		final User stored = userRepository.findOne(user.getId());
//
//		switch (targetType) {
//		case "event":
//			if ("create".equals(permission))
//				return stored.getEvents().size() < stored.getLicense().getSubscription().getEventLimit();
//		case "page":
//			if ("create".equals(permission))
//				return stored.getPages().size() < stored.getLicense().getSubscription().getPageLimit();
//
//		}

		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {

		User user = (User) authentication.getPrincipal();
		final User stored = userRepository.findOne(user.getId());

		switch (targetType) {
		case "book_request":
			BookRequest bookRequest = bookRequestRepository.findOne((Long) targetId);
			return bookRequest.getService().getPage().getPageAdministrators().stream()
					.anyMatch(x -> x.getUser().getId() == stored.getId());
		case "conference":
			Conference conference = conferenceRepository.findOne((Long) targetId);

			if (permission.equals("update"))
				return checkConferenceOwnership(conference, stored);
			else
				return conference.getVisibility() == Visibility.PUBLIC
						|| conference.getOwner().getId() == stored.getId();
		case "event":
			Event event = eventRepository.findOne((Long) targetId);

			if (permission.equals("update")) {
				return checkEventOwnership(event, stored);
			} else
				return checkEventVisibility(event, stored);
		case "license":
			return true;
		case "notification":
			Notification notification = notificationRepository.findOne((Long) targetId);
			return notification.getRecipient().getId() == stored.getId();
		case "page":
			Page page = pageRepository.findOne((Long) targetId);
			if (permission.equals("update")) {
				return checkPageOwnership(page, stored);
			} else
				return true;
		case "programme":
			Programme programme = programmeRepository.findOne((Long) targetId);
			return checkEventOwnership(programme.getEvent(), stored);
		case "service":
			Service service = serviceRepository.findOne((Long) targetId);

			if (permission == "update")
				return service.getPage().getPageAdministrators().stream()
						.anyMatch(x -> x.getUser().getId() == stored.getId());
			else
				return true;
		case "page-administrator":
			return true;
		}

		return false;
	}

	private boolean checkEventVisibility(Event event, User user) {
		// TODO : implement invitations;
		return event.getVisibility() == Visibility.PUBLIC || checkEventOwnership(event, user);
	}

	private boolean checkEventOwnership(Event event, User user) {
		if (event.getOwner() != null)
			return event.getOwner().getId() == user.getId();
		else if (event.getPage() != null)
			return checkPageOwnership(event.getPage(), user);
		else if (event.getConference() != null)
			return checkConferenceOwnership(event.getConference(), user);
		else
			return false;
	}

	private boolean checkPageOwnership(Page page, User user) {
		Optional<PageAdministrator> oPa = page.getPageAdministrators().stream()
				.filter(x -> x.getUser().getId() == user.getId()).findFirst();

		if (oPa.isPresent()) {
			PageAdministrator pa = oPa.get();
			return pa.getActive() && (pa.getRole() == PageRole.ROLE_ADMINISTRATOR || pa.getRole() == PageRole.ROLE_OWNER);
		}

		return false;
	}

	private boolean checkConferenceOwnership(Conference conference, User user) {
		return conference.getOwner().getId() == user.getId();
	}
}
