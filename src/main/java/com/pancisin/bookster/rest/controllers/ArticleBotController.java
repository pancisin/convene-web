package com.pancisin.bookster.rest.controllers;

import java.security.Principal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.components.ArticleBotService;
import com.pancisin.bookster.model.ArticleBot;
import com.pancisin.bookster.model.ArticleBotRun;
import com.pancisin.bookster.model.enums.BotRunState;
import com.pancisin.bookster.repository.ArticleBotRepository;

@RestController
//@PreAuthorize("hasRole('SUPERADMIN')")
@RequestMapping("/api/article-bot/{articleBotId}")
public class ArticleBotController {

	@Autowired
	private ArticleBotRepository abRepository;

	@Autowired
	private ArticleBotService articleBotService;

	@Autowired
	private SimpMessagingTemplate webSocket;

	@GetMapping
	public ResponseEntity<?> getArticleBot(@PathVariable UUID articleBotId) {
		ArticleBot stored = abRepository.findOne(articleBotId);
		return ResponseEntity.ok(stored);
	}

	@PutMapping
	public ResponseEntity<?> putArticleBot(@PathVariable UUID articleBotId, @RequestBody ArticleBot articleBot) {
		ArticleBot stored = abRepository.findOne(articleBotId);

		stored.setName(articleBot.getName());
		stored.setActive(articleBot.getActive());
		stored.setParser(articleBot.getParser());
		stored.setSourceType(articleBot.getSourceType());
		stored.setSourceUrl(articleBot.getSourceUrl());

		return ResponseEntity.ok(abRepository.save(stored));
	}

	@DeleteMapping
	public ResponseEntity<?> deleteArticleBot(@PathVariable UUID articleBotId) {
		abRepository.delete(articleBotId);
		return ResponseEntity.ok("success");
	}

	@PatchMapping("/toggle-active")
	public ResponseEntity<?> toggleActive(@PathVariable UUID articleBotId) {
		ArticleBot stored = abRepository.findOne(articleBotId);
		stored.setActive(!stored.getActive());
		return ResponseEntity.ok(abRepository.save(stored));
	}

	@GetMapping("/run")
	public ResponseEntity<?> getRuns(@PathVariable UUID articleBotId) {
		ArticleBot stored = abRepository.findOne(articleBotId);
		return ResponseEntity.ok(stored.getRuns());
	}

	@MessageMapping("/article-bot/{bot_id}/run")
	public void runEventBot(@DestinationVariable("bot_id") UUID bot_id, Principal principal) {
		ArticleBot stored = abRepository.findOne(bot_id);

		webSocket.convertAndSendToUser(principal.getName(), "/queue/list.bots",
				new ArticleBotRun(stored, BotRunState.RUNNING));

		ArticleBotRun run = articleBotService.run(stored);
		webSocket.convertAndSendToUser(principal.getName(), "/queue/list.bots", run);
	}
}
