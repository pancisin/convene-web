package com.pancisin.bookster.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.components.EmailService;
import com.pancisin.bookster.components.Notifier;
import com.pancisin.bookster.models.BookRequest;
import com.pancisin.bookster.models.Service;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.repository.BookRequestRepository;
import com.pancisin.bookster.repository.ServiceRepository;

@RestController
@RequestMapping("/api/service/{service_id}")
public class ServiceController {

	@Autowired
	private ServiceRepository serviceRepository;

	@Autowired
	private BookRequestRepository bookRequestRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private Notifier notifier;

	@GetMapping
	@PreAuthorize("hasPermission(#service_id, 'service', 'read')")
	public ResponseEntity<?> getService(@PathVariable Long service_id) {
		return ResponseEntity.ok(serviceRepository.findOne(service_id));
	}

	@DeleteMapping
	@PreAuthorize("hasPermission(#service_id, 'service', 'update')")
	public ResponseEntity<?> deleteService(@PathVariable Long service_id) {
		serviceRepository.delete(service_id);
		return ResponseEntity.ok("success");
	}

	@PutMapping
	@PreAuthorize("hasPermission(#service_id, 'service', 'update')")
	public ResponseEntity<?> putService(@PathVariable Long service_id, @RequestBody Service service) {
		Service stored = serviceRepository.findOne(service_id);
		stored.setName(service.getName());
		stored.setDetail(service.getDetail());
		stored.setPricePerUnit(service.getPricePerUnit());
		stored.setUnit(service.getUnit());
		return ResponseEntity.ok(serviceRepository.save(stored));
	}

	@PostMapping("/request")
	@PreAuthorize("hasPermission(#service_id, 'service', 'read')")
	public ResponseEntity<?> postRequest(@PathVariable Long service_id, @RequestBody BookRequest request) {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Service service = serviceRepository.findOne(service_id);
		request.setUser(auth);
		request.setService(service);
		request.setApproved(false);

		String message = "User " + auth.getUsername() + " requested " + request.getUnits() + " units of "
				+ service.getName();
		service.getPage().getPageAdministrators().stream().forEach(x -> {
			emailService.sendSimpleMessage(x.getUser().getEmail(), "Service request", message);
			notifier.notifyUser(x.getUser(), "Service request", message);
		});

		return ResponseEntity.ok(bookRequestRepository.save(request));
	}
	
	@GetMapping("/request")
	@PreAuthorize("hasPermission(#service_id, 'service', 'read')")
	public ResponseEntity<?> getRequests(@PathVariable Long service_id) {
		Service service = serviceRepository.findOne(service_id);
		return ResponseEntity.ok(service.getRequests());
	}
}
