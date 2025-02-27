package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pancisin.bookster.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	@Query("SELECT DISTINCT category FROM Category category JOIN category.branches branch RIGHT JOIN branch.pages page WHERE page IS NOT NULL AND (page.state = 'PUBLISHED' OR page.state = 'BLOCKED')")
	public List<Category> getUsed();
}
