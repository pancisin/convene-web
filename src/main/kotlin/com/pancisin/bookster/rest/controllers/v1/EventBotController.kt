package com.pancisin.bookster.rest.controllers.v1

import java.security.Principal
import java.util.UUID

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate

import com.pancisin.bookster.services.EventBotService
import com.pancisin.bookster.model.EventBot
import com.pancisin.bookster.model.BotRun
import com.pancisin.bookster.model.enums.BotRunState
import com.pancisin.bookster.repository.EventBotRepository
import com.pancisin.bookster.repository.BotRunRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
// @PreAuthorize("hasRole('SUPERADMIN')")
@RequestMapping("/api/v1/event-bot/{bot_id}")
class EventBotController {

  @Autowired
  lateinit var eventBotRepository: EventBotRepository

  @Autowired
  lateinit var botRunRepository: BotRunRepository

  @Autowired
  lateinit var eventBotService: EventBotService

  @Autowired
  lateinit var webSocket: SimpMessagingTemplate

  @GetMapping
  fun getEventBot(@PathVariable bot_id: UUID) = ResponseEntity.ok(eventBotRepository.findOne(bot_id))

  @DeleteMapping
  fun deleteMapping(@PathVariable bot_id: UUID): ResponseEntity<String> {
    eventBotRepository.delete(bot_id)
    return ResponseEntity.ok("success")
  }

  @PutMapping
  fun putEventBot(@PathVariable bot_id: UUID, @RequestBody @Valid eventBot: EventBot): ResponseEntity<EventBot>? {
    val stored = eventBotRepository.findOne(bot_id)
    return ResponseEntity.ok(eventBotRepository.save(stored.apply {
      name = eventBot.name
      fbPageId = eventBot.fbPageId
    }))
  }

  @PatchMapping("/toggle-active")
  fun toggleActive(@PathVariable bot_id: UUID): ResponseEntity<EventBot> {
    val stored = eventBotRepository.findOne(bot_id).apply {
      active = !active
    }
    return ResponseEntity.ok(eventBotRepository.save(stored))
  }

  @GetMapping("/run/{page}/{size}")
  fun getRuns(
    @PathVariable bot_id: UUID,
    @PathVariable page: Int,
    @PathVariable size: Int
  ): ResponseEntity<Page<BotRun>> {
    return ResponseEntity.ok(botRunRepository.getByEventBot(bot_id, PageRequest(page, size, Sort.Direction.DESC, "date")))
  }

  @MessageMapping("/bot/{bot_id}/run")
  fun runEventBot(@DestinationVariable("bot_id") bot_id: UUID, principal: Principal) {
    val stored = eventBotRepository.findOne(bot_id)

    webSocket.convertAndSendToUser(principal.name, "/queue/page.bots", BotRun(stored, BotRunState.RUNNING))

    val run = eventBotService.run(stored)
    webSocket.convertAndSendToUser(principal.name, "/queue/page.bots", run)
  }
}
