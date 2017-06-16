package com.pancisin.bookster.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.repository.InvitationRepository;

@RestController
@PreAuthorize("hasPermission(#invitation_id, 'invitation', '')")
@RequestMapping("/api/invitation/{invitation_id}")
public class InvitationController {

	@Autowired
	private InvitationRepository invitationRepository;

	@GetMapping
	public ResponseEntity<?> getInvitation(@PathVariable Long invitation_id) {
		return ResponseEntity.ok(invitationRepository.findOne(invitation_id));
	}

	@DeleteMapping
	public ResponseEntity<?> deleteInvitation(@PathVariable Long invitation_id) {
		invitationRepository.delete(invitation_id);
		return ResponseEntity.ok("success");
	}
}
