package com.pancisin.bookster.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.model.Category;
import com.pancisin.bookster.repository.CategoryRepository;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping
	public ResponseEntity<?> getCategories() {
		return ResponseEntity.ok(categoryRepository.findAll());
	}

	@GetMapping("/{category_id}/branches")
	public ResponseEntity<?> getBraches(@PathVariable Long category_id) {
		Category stored = categoryRepository.findOne(category_id);
		return ResponseEntity.ok(stored.getBranches());
	}
}
