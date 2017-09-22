package com.pancisin.bookster.rest.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.components.EventBotService;
import com.pancisin.bookster.models.EventBot;
import com.pancisin.bookster.repository.EventBotRepository;

@RestController
@RequestMapping("/api/event-bot/{bot_id}")
public class EventBotController {

	@Autowired
	private EventBotRepository eventBotRepository;

	@Autowired
	private EventBotService eventBotService;
	
	@GetMapping
	public ResponseEntity<?> getEventBot(@PathVariable UUID bot_id) {
		return ResponseEntity.ok(eventBotRepository.findOne(bot_id));
	}

	@DeleteMapping
	public ResponseEntity<?> deleteMapping(@PathVariable UUID bot_id) {
		eventBotRepository.delete(bot_id);
		return ResponseEntity.ok("success");
	}

	@PatchMapping("/toggle-active")
	public ResponseEntity<?> toggleActive(@PathVariable UUID bot_id) {
		EventBot stored = eventBotRepository.findOne(bot_id);
		stored.setActive(!stored.isActive());
		return ResponseEntity.ok(eventBotRepository.save(stored));
	}
	
	@PostMapping("/run") 
	public ResponseEntity<?> runBot(@PathVariable UUID bot_id) {
		EventBot stored = eventBotRepository.findOne(bot_id);
		return ResponseEntity.ok(eventBotService.run(stored));
	}
	
	@GetMapping("/run") 
	public ResponseEntity<?> getRuns(@PathVariable UUID bot_id) {
		EventBot stored = eventBotRepository.findOne(bot_id);
		return ResponseEntity.ok(stored.getRuns());
	}
}
