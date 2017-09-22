package com.pancisin.bookster.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.models.EventBotRun;

public interface EventBotRunRepository extends JpaRepository<EventBotRun, UUID> {

}
