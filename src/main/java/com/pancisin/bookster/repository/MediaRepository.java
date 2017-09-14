package com.pancisin.bookster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.models.Media;

public interface MediaRepository extends JpaRepository<Media, Long> {

}
