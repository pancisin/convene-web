package com.pancisin.bookster.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.model.EventBotRun;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventBotRunRepository extends JpaRepository<EventBotRun, UUID> {

  @Query("SELECT run FROM EventBotRun run WHERE run.bot.id = :botId")
  Page<EventBotRun> getByEventBot(@Param("botId") UUID botId, Pageable pageable);
}
