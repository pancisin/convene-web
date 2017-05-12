package com.pancisin.bookster.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.Service;
import com.pancisin.bookster.repository.ServiceRepository;

@RestController
@PreAuthorize("hasPermission(#service_id, 'service', '')")
@RequestMapping("/api/service/{service_id}")
public class ServiceController {

	@Autowired
	private ServiceRepository serviceRepository;
	
	@GetMapping
	public ResponseEntity<?> getService(@PathVariable Long service_id) {
		return ResponseEntity.ok(serviceRepository.findOne(service_id));
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteService(@PathVariable Long service_id) {
		serviceRepository.delete(service_id);
		return ResponseEntity.ok("success");
	}
	
	@PutMapping
	public ResponseEntity<?> putService(@PathVariable Long service_id, @RequestBody Service service) {
		Service stored = serviceRepository.findOne(service_id);
		stored.setName(service.getName());
		stored.setDuration(service.getDuration());
		stored.setPrice(service.getPrice());
		return ResponseEntity.ok(serviceRepository.save(stored));
	}
}
