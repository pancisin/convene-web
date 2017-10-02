package com.pancisin.bookster.rest.controllers;

import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.pancisin.bookster.models.ArticlesList;
import com.pancisin.bookster.repository.ArticleRepository;
import com.pancisin.bookster.repository.ArticlesListRepository;

@RestController
@RequestMapping("/api/articles-list/{articlesListId}")
public class ArticlesListController {

	@Autowired
	private ArticlesListRepository alRepository;
	
	@Autowired
	private ArticleRepository articleRepository;

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
		return ResponseEntity.ok(alRepository.save(stored));
	}

	@GetMapping("/article")
	public ResponseEntity<?> getArticles(@PathVariable UUID articlesListId) {
		return ResponseEntity.ok(alRepository.findOne(articlesListId).getArticles());
	}

	@Transactional
	@PostMapping("/article")
	public ResponseEntity<?> postArticle(@PathVariable UUID articlesListId, @RequestBody Article article) {
		ArticlesList stored = alRepository.findOne(articlesListId);

		article = articleRepository.save(article);
		stored.addArticle(article);
		alRepository.save(stored);
		
		return ResponseEntity.ok(article);
	}
}
