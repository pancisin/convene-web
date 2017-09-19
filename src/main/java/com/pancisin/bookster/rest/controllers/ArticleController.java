package com.pancisin.bookster.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.components.storage.StorageServiceImpl;
import com.pancisin.bookster.models.Article;
import com.pancisin.bookster.models.Media;
import com.pancisin.bookster.repository.ArticleRepository;
import com.pancisin.bookster.repository.MediaRepository;

@RestController
@RequestMapping("/api/article/{article_id}")
public class ArticleController {

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private StorageServiceImpl storageService;

	@Autowired
	private MediaRepository mediaRepository;
	
	@GetMapping
	public ResponseEntity<?> getArticle(@PathVariable Long article_id) {
		return ResponseEntity.ok(articleRepository.findOne(article_id));
	}

	@PutMapping
	public ResponseEntity<?> putArticle(@PathVariable Long article_id, @RequestBody Article article) {
		Article stored = articleRepository.findOne(article_id);

		stored.setContent(article.getContent());
		stored.setTitle(article.getTitle());

		if (article.getThumbnailData() != null && storageService.isBinary(article.getThumbnailData())) {
			Media thumbnail = new Media();
			thumbnail = mediaRepository.save(thumbnail);
			String url = "banners/articles/" + thumbnail.getId().toString();
			
			thumbnail.setPath("/files/" + url + ".jpg");
			storageService.storeBinary(article.getThumbnailData(), url);
			stored.setThumbnail(thumbnail);
		}

		return ResponseEntity.ok(articleRepository.save(stored));
	}

	@DeleteMapping
	public ResponseEntity<?> deleteArticle(@PathVariable Long article_id) {
		articleRepository.delete(article_id);
		return ResponseEntity.ok("success");
	}

	@PatchMapping("/toggle-published")
	public ResponseEntity<?> togglePublished(@PathVariable Long article_id) {
		Article stored = articleRepository.findOne(article_id);
		stored.setPublished(!stored.isPublished());
		return ResponseEntity.ok(articleRepository.save(stored));
	}
}
