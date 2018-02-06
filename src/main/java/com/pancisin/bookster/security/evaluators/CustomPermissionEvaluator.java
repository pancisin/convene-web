package com.pancisin.bookster.security.evaluators;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

import com.pancisin.bookster.model.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.pancisin.bookster.model.ServiceRequest;
import com.pancisin.bookster.model.Event;
import com.pancisin.bookster.model.Notification;
import com.pancisin.bookster.model.Page;
import com.pancisin.bookster.model.Programme;
import com.pancisin.bookster.model.Service;
import com.pancisin.bookster.model.User;
import com.pancisin.bookster.model.enums.PageRole;
import com.pancisin.bookster.model.enums.PageState;
import com.pancisin.bookster.model.enums.Visibility;
import com.pancisin.bookster.repository.ServiceRequestRepository;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.NotificationRepository;
import com.pancisin.bookster.repository.AdministratorRepository;
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
	private ProgrammeRepository programmeRepository;

	@Autowired
	private ServiceRequestRepository serviceRequestRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private PageRepository pageRepository;

	@Autowired
	private ServiceRepository serviceRepository;

	@Autowired
	private AdministratorRepository paRepository;

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
			ServiceRequest serviceRequest = serviceRequestRepository.findOne((Long) targetId);
			return serviceRequest.getService().getPage().getAdministrators().stream()
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

			Notification notification = notificationRepository.findOne((UUID) targetId);
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
				return page.getState() == PageState.PUBLISHED || page.getState() == PageState.BLOCKED || checkPageOwnership(page, userId);
			}

		case "programme":
			Programme programme = programmeRepository.findOne((Long) targetId);
			return checkEventOwnership(programme.getEvent(), userId);

		case "service":
			Service service = serviceRepository.findOne((Long) targetId);

			if (permission == "update")
				return service.getPage().getAdministrators().stream().anyMatch(x -> x.getUser().getId() == userId);
			else
				return true;

		case "administrator":
			return true;

		case "invitation":
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
		else
			return false;
	}

	private boolean checkPageOwnership(Page page, Long userId) {
		Optional<Administrator> oPa = page.getAdministrators().stream().filter(x -> x.getUser().getId() == userId)
				.findFirst();

		if (oPa.isPresent()) {
			Administrator pa = oPa.get();
			return pa.getActive() && (pa.getRole() == PageRole.ROLE_ADMINISTRATOR || pa.getRole() == PageRole.ROLE_OWNER);
		}

		return false;
	}

//	private boolean checkPageVisibility(Page page, Long userId) {
//		if (page.getState() == PageState.PUBLISHED || page.getState() == PageState.BLOCKED) {
//			return true;
//		} else if (page.getVisibility() == Visibility.INVITED) {
//			return page.getInvitations().stream().anyMatch(i -> i.getUser().getId() == userId);
//		}
//
//		return false;
//	}
}
