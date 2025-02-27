package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.model.Branch;

public interface BranchRepository extends JpaRepository<Branch, Long> {

	@Query("SELECT DISTINCT branch FROM Branch branch JOIN branch.category category RIGHT JOIN branch.pages page WHERE page IS NOT NULL AND category.id = :category_id AND (page.state = 'PUBLISHED' OR page.state = 'BLOCKED')")
	public List<Branch> getUsed(@Param("category_id") Long category_id);
}
