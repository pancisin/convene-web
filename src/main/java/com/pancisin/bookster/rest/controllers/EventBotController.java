package com.pancisin.bookster.rest.controllers;

import java.security.Principal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.components.EventBotService;
import com.pancisin.bookster.models.EventBot;
import com.pancisin.bookster.models.EventBotRun;
import com.pancisin.bookster.models.enums.BotRunState;
import com.pancisin.bookster.repository.EventBotRepository;

@RestController
//@PreAuthorize("hasRole('SUPERADMIN')")
@RequestMapping("/api/event-bot/{bot_id}")
public class EventBotController {

	@Autowired
	private EventBotRepository eventBotRepository;

	@Autowired
	private EventBotService eventBotService;
	
	@Autowired
	private SimpMessagingTemplate webSocket;
	
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
	
	@MessageMapping("/bot/{bot_id}/run")
	public void runEventBot(@DestinationVariable("bot_id") UUID bot_id, Principal principal) {
		EventBot stored = eventBotRepository.findOne(bot_id);

		webSocket.convertAndSendToUser(principal.getName(), "/queue/page.bots", new EventBotRun(stored, BotRunState.RUNNING));
		
		EventBotRun run = eventBotService.run(stored);
		webSocket.convertAndSendToUser(principal.getName(), "/queue/page.bots", run);
	}
}
