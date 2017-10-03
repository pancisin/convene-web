package com.pancisin.bookster.rest.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.components.ArticleBotService;
import com.pancisin.bookster.models.ArticleBot;
import com.pancisin.bookster.repository.ArticleBotRepository;

@RestController
@RequestMapping("/api/article-bot/{articleBotId}")
public class ArticleBotController {

	
	@Autowired
	private ArticleBotRepository abRepository;
	
	@Autowired
	private ArticleBotService articleBotService;
	
	@GetMapping
	public ResponseEntity<?> getArticleBot(@PathVariable UUID articleBotId) {
		ArticleBot stored = abRepository.findOne(articleBotId);
		return ResponseEntity.ok(stored);
	}
	
	@PutMapping
	public ResponseEntity<?> putArticleBot(@PathVariable UUID articleBotId, @RequestBody ArticleBot articleBot) {
		ArticleBot stored = abRepository.findOne(articleBotId);

		stored.setName(articleBot.getName());
		stored.setActive(articleBot.isActive());
		stored.setParser(articleBot.getParser());
		stored.setSourceType(articleBot.getSourceType());
		stored.setSourceUrl(articleBot.getSourceUrl());

		return ResponseEntity.ok(abRepository.save(stored));
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteArticleBot(@PathVariable UUID articleBotId) {
		return null;
	}
	
	@GetMapping("/run")
	public ResponseEntity<?> runArticleBot(@PathVariable UUID articleBotId) {
		ArticleBot stored = abRepository.findOne(articleBotId);
		
		articleBotService.run(stored);
		return ResponseEntity.ok("ok");
	}
}
