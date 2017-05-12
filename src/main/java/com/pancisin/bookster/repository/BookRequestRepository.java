package com.pancisin.bookster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.models.BookRequest;

public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {

}
