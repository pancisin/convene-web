package com.pancisin.bookster.rest.controllers;

import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.Article;
import com.pancisin.bookster.models.ArticleBot;
import com.pancisin.bookster.models.ArticlesList;
import com.pancisin.bookster.repository.ArticleBotRepository;
import com.pancisin.bookster.repository.ArticleRepository;
import com.pancisin.bookster.repository.ArticlesListRepository;

@RestController
@RequestMapping("/api/articles-list/{articlesListId}")
public class ArticlesListController {

	@Autowired
	private ArticlesListRepository alRepository;
	
	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ArticleBotRepository abRepository;
	
	@GetMapping
	public ResponseEntity<?> getArticlesList(@PathVariable UUID articlesListId) {
		return ResponseEntity.ok(alRepository.findOne(articlesListId));
	}

	@PutMapping
	public ResponseEntity<?> putArticlesList(@PathVariable UUID articlesListId, @RequestBody @Valid ArticlesList articlesList, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {

		}
		
		ArticlesList stored = alRepository.findOne(articlesListId);
		stored.setName(articlesList.getName());
		stored.setTags(articlesList.getTags());
		return ResponseEntity.ok(alRepository.save(stored));
	}

	@GetMapping("/article/{page}/{size}")
	public ResponseEntity<?> getArticles(@PathVariable UUID articlesListId, @PathVariable int page, @PathVariable int size) {
		return ResponseEntity.ok(articleRepository.getByArticlesList(articlesListId, new PageRequest(page, size, Direction.DESC, "created")));
	}

	@Transactional
	@PostMapping("/article")
	public ResponseEntity<?> postArticle(@PathVariable UUID articlesListId, @RequestBody Article article) {
		ArticlesList stored = alRepository.findOne(articlesListId);
		article.setArticlesList(stored);
		
		return ResponseEntity.ok(articleRepository.save(article));
	}
	
	@PostMapping("/bot")
	public ResponseEntity<?> postArticleBot(@PathVariable UUID articlesListId, @RequestBody ArticleBot bot) {
		ArticlesList stored = alRepository.findOne(articlesListId);
		bot.setArticlesList(stored);
		return ResponseEntity.ok(abRepository.save(bot));
	}

	@GetMapping("/bot")
	public ResponseEntity<?> getArticleBots(@PathVariable UUID articlesListId) {
		return ResponseEntity.ok(abRepository.getByList(articlesListId));
	}
}
