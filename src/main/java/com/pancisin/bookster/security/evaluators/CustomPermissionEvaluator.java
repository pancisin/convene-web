package com.pancisin.bookster.security.evaluators;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.pancisin.bookster.models.BookRequest;
import com.pancisin.bookster.models.Conference;
import com.pancisin.bookster.models.ConferenceAdministrator;
import com.pancisin.bookster.models.Event;
import com.pancisin.bookster.models.Notification;
import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.PageAdministrator;
import com.pancisin.bookster.models.Programme;
import com.pancisin.bookster.models.Service;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.models.enums.PageRole;
import com.pancisin.bookster.models.enums.PageState;
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
		// String targetType = (String) targetDomainObject;
		//
		// User user = (User) authentication.getPrincipal();
		// final User stored = userRepository.findOne(user.getId());
		//
		// switch (targetType) {
		// case "event":
		// if ("create".equals(permission))
		// return stored.getEvents().size() <
		// stored.getLicense().getSubscription().getEventLimit();
		// case "page":
		// if ("create".equals(permission))
		// return stored.getPages().size() <
		// stored.getLicense().getSubscription().getPageLimit();
		//
		// }

		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {

		boolean authenticated = !(authentication.getPrincipal() instanceof String);
		Long userId = authenticated ? ((User) authentication.getPrincipal()).getId() : null;

		switch (targetType) {
		case "book_request":
			BookRequest bookRequest = bookRequestRepository.findOne((Long) targetId);
			return bookRequest.getService().getPage().getPageAdministrators().stream()
					.anyMatch(x -> x.getUser().getId() == userId);

		case "event":
			Event event = eventRepository.findOne((Long) targetId);

			if (event == null)
				return true;

			if (authenticated) {
				if (permission.equals("update")) {
					return checkEventOwnership(event, userId);
				} else
					return checkEventVisibility(event, userId);
			} else {
				return event.getVisibility() == Visibility.PUBLIC
						&& (event.getState() == PageState.PUBLISHED || event.getState() == PageState.BLOCKED);
			}

		case "license":
			return authenticated;

		case "notification":
			if (!authenticated)
				return false;

			Notification notification = notificationRepository.findById((UUID) targetId);
			return notification.getRecipient().getId() == userId;

		case "page":
			Page page = null;
			if (targetId instanceof String) {
				page = pageRepository.findBySlug((String) targetId);
			} else {
				page = pageRepository.findOne((Long) targetId);
			}

			if (page == null)
				return true;

			if (authenticated) {
				if (permission.equals("admin-read")) {
					return checkPageOwnership(page, userId);
				} else if (permission.equals("update")) {
					return page.getState() != PageState.BLOCKED && checkPageOwnership(page, userId);
				}
			}

			if (permission.equals("read")) {
				return page.getState() == PageState.PUBLISHED || page.getState() == PageState.BLOCKED;
			}
		case "conference":
			Conference conference = conferenceRepository.findOne((Long) targetId);

			if (conference == null)
				return true;

			if (authenticated) {
				if (permission.equals("admin-read"))
					return checkConferenceOwnership(conference, userId);
				else if (permission.equals("update"))
					return conference.getState() != PageState.BLOCKED && checkConferenceOwnership(conference, userId);
				else
					return checkConferenceVisibility(conference, userId) || checkConferenceOwnership(conference, userId);
			} else {
				return conference.getVisibility() == Visibility.PUBLIC
						&& (conference.getState() == PageState.PUBLISHED || conference.getState() == PageState.BLOCKED);
			}

		case "programme":
			Programme programme = programmeRepository.findOne((Long) targetId);
			return checkEventOwnership(programme.getEvent(), userId);

		case "service":
			Service service = serviceRepository.findOne((Long) targetId);

			if (permission == "update")
				return service.getPage().getPageAdministrators().stream().anyMatch(x -> x.getUser().getId() == userId);
			else
				return true;

		case "page-administrator":
			return true;

		case "invitation":
			return true;

		case "conference-administrator":
			return true;
		}

		return false;
	}

	private boolean checkEventVisibility(Event event, Long userId) {
		return (event.getVisibility() == Visibility.PUBLIC && event.getState() == PageState.PUBLISHED)
				|| checkEventOwnership(event, userId)
				|| event.getInvitations().stream().anyMatch(x -> x.getUser().getId() == userId);
	}

	private boolean checkEventOwnership(Event event, Long userId) {
		if (event.getOwner() != null)
			return event.getOwner().getId() == userId;
		else if (event.getPage() != null)
			return checkPageOwnership(event.getPage(), userId);
		else if (event.getConference() != null)
			return checkConferenceOwnership(event.getConference(), userId);
		else
			return false;
	}

	private boolean checkPageOwnership(Page page, Long userId) {
		Optional<PageAdministrator> oPa = page.getPageAdministrators().stream().filter(x -> x.getUser().getId() == userId)
				.findFirst();

		if (oPa.isPresent()) {
			PageAdministrator pa = oPa.get();
			return pa.getActive() && (pa.getRole() == PageRole.ROLE_ADMINISTRATOR || pa.getRole() == PageRole.ROLE_OWNER);
		}

		return false;
	}

	private boolean checkConferenceOwnership(Conference conference, Long userId) {
		Optional<ConferenceAdministrator> oCa = conference.getConferenceAdministrators().stream()
				.filter(x -> x.getUser().getId() == userId).findFirst();

		if (oCa.isPresent()) {
			ConferenceAdministrator ca = oCa.get();
			return ca.isActive() && (ca.getRole() == PageRole.ROLE_ADMINISTRATOR || ca.getRole() == PageRole.ROLE_OWNER);
		}

		return false;
	}

	private boolean checkConferenceVisibility(Conference conference, Long userId) {
		if (conference.getVisibility() == Visibility.PUBLIC) {
			return true;
		} else if (conference.getVisibility() == Visibility.INVITED) {
			return conference.getInvitations().stream().anyMatch(i -> i.getUser().getId() == userId);
		}

		return false;
	}
}
