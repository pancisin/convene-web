package com.pancisin.bookster.rest.controllers.v1

import java.security.Principal
import java.util.UUID

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.pancisin.bookster.components.ArticleBotService
import com.pancisin.bookster.model.ArticleBot
import com.pancisin.bookster.model.ArticleBotRun
import com.pancisin.bookster.model.enums.BotRunState
import com.pancisin.bookster.repository.ArticleBotRepository
import com.pancisin.bookster.repository.ArticleBotRunRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

@RestController
//@PreAuthorize("hasRole('SUPERADMIN')")
@RequestMapping("/api/v1/article-bot/{articleBotId}")
class ArticleBotController {

  @Autowired
  lateinit var abRepository: ArticleBotRepository

  @Autowired
  lateinit var abrRepository: ArticleBotRunRepository

  @Autowired
  lateinit var articleBotService: ArticleBotService

  @Autowired
  lateinit var webSocket: SimpMessagingTemplate

  @GetMapping
  fun getArticleBot(@PathVariable articleBotId: UUID) = ResponseEntity.ok(abRepository.findOne(articleBotId))

  @PutMapping
  fun putArticleBot(@PathVariable articleBotId: UUID, @RequestBody articleBot: ArticleBot): ResponseEntity<ArticleBot> {
    val stored = abRepository.findOne(articleBotId).apply {
      name = articleBot.name;
      active = articleBot.active;
      parser = articleBot.parser;
      sourceType = articleBot.sourceType;
      sourceUrl = articleBot.sourceUrl
    }

    return ResponseEntity.ok(abRepository.save(stored))
  }

  @DeleteMapping
  fun deleteArticleBot(@PathVariable articleBotId: UUID): ResponseEntity<String> {
    abRepository.delete(articleBotId)
    return ResponseEntity.ok("success")
  }

  @PatchMapping("/toggle-active")
  fun toggleActive(@PathVariable articleBotId: UUID): ResponseEntity<ArticleBot> {
    val stored = abRepository.findOne(articleBotId)
    stored.active = !stored.active
    return ResponseEntity.ok(abRepository.save(stored))
  }

  @GetMapping("/run/{page}/{size}")
  fun getRuns(
    @PathVariable articleBotId: UUID,
    @PathVariable page: Int,
    @PathVariable size: Int): ResponseEntity<Page<ArticleBotRun>> {
    return ResponseEntity.ok(abrRepository.getByArticleBot(articleBotId, PageRequest(page, size, Sort.Direction.DESC, "date")))
  }

  @MessageMapping("/article-bot/{bot_id}/run")
  fun runEventBot(@DestinationVariable("bot_id") bot_id: UUID, principal: Principal) {
    val stored = abRepository.findOne(bot_id)

    webSocket.convertAndSendToUser(principal.name, "/queue/list.bots", ArticleBotRun(stored, BotRunState.RUNNING))

    val run = articleBotService.run(stored)
    webSocket.convertAndSendToUser(principal.name, "/queue/list.bots", run)
  }
}

