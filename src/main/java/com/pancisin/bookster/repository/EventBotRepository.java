package com.pancisin.bookster.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.models.EventBot;

public interface EventBotRepository extends JpaRepository<EventBot, UUID> {

	@Query("SELECT eventBot from EventBot eventBot WHERE eventBot.page.id = :page_id")
	public Page<EventBot> getByPage(@Param("page_id") Long page_id, Pageable pageable);
	
	public EventBot getById(@Param("bot_id") UUID bot_id);
}
