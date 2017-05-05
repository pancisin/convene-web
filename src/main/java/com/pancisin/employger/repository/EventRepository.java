package com.pancisin.employger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.employger.models.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

}
