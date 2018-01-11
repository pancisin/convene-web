package com.pancisin.bookster.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.model.BotRun;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BotRunRepository extends JpaRepository<BotRun, UUID> {

  @Query("SELECT run FROM BotRun run WHERE run.eventBot.id = :botId")
  Page<BotRun> getByEventBot(@Param("botId") UUID botId, Pageable pageable);

  @Query("SELECT run FROM BotRun run WHERE run.articleBot.id = :botId")
  Page<BotRun> getByArticleBot(@Param("botId") UUID botId, Pageable pageable);
}
