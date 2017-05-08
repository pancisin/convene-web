package com.pancisin.bookster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.models.Conference;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {

}
