package com.pancisin.bookster.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.BookRequest;
import com.pancisin.bookster.repository.BookRequestRepository;

@RestController
@PreAuthorize("hasPermission(#book_request_id, 'book_request', '')")
@RequestMapping("/api/book-request/{book_request_id}")
public class BookRequestController {

	@Autowired
	private BookRequestRepository bookRequestRepository;
	
	@PatchMapping
	public ResponseEntity<?> approveBookRequest(@PathVariable Long book_request_id) {
		BookRequest request = bookRequestRepository.findOne(book_request_id);
		request.setApproved(true);
		return ResponseEntity.ok(bookRequestRepository.save(request));
	}
	
	@GetMapping
	public ResponseEntity<?> getBookRequest(@PathVariable Long book_request_id) {
		return ResponseEntity.ok(bookRequestRepository.findOne(book_request_id));
	}
}
