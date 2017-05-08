package com.pancisin.bookster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.models.Page;

public interface PageRepository extends JpaRepository<Page, Long> {

}
