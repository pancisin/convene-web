package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.models.Branch;
import com.pancisin.bookster.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	@Query("SELECT DISTINCT category FROM Category category JOIN category.branches branch RIGHT JOIN branch.pages page WHERE page IS NOT NULL")
	public List<Category> getUsed();
}
