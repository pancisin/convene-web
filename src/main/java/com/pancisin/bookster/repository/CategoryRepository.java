package com.pancisin.bookster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
