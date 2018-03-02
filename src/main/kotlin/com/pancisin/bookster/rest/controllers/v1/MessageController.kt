package com.pancisin.bookster.rest.controllers.v1

import com.pancisin.bookster.model.Conversation
import java.security.Principal

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.pancisin.bookster.model.Message
import com.pancisin.bookster.model.User
import com.pancisin.bookster.model.enums.RecipientType
import com.pancisin.bookster.repository.MessageRepository
import com.pancisin.bookster.repository.UserRepository

@RestController
@RequestMapping("/api/v1/message")
class MessageController {

  private val pageLimit = 10

  @Autowired
  lateinit var messageRepository: MessageRepository

  @Autowired
  lateinit var userRepository: UserRepository

  @GetMapping("/conversations")
  fun getConversations(): ResponseEntity<List<Conversation>> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    return ResponseEntity.ok(messageRepository.getConversations(auth.id))
  }

  @GetMapping("/user/{user_id}/{page}")
  fun getPrivateConversation(@PathVariable user_id: Long?, @PathVariable page: Int): ResponseEntity<*> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    return ResponseEntity.ok(messageRepository.getPrivate(auth.id, user_id, PageRequest(page, pageLimit)))
  }

  @GetMapping("/page/{page_id}/{page}")
  fun getPageConversations(
    @PathVariable page_id: Long?,
    @PathVariable page: Int
  ) = ResponseEntity.ok(messageRepository.getPageMessages(page_id, PageRequest(page, pageLimit)))

  @GetMapping("/event/{event_id}/{page}")
  fun getEventConversations(
    @PathVariable event_id: Long?,
    @PathVariable page: Int
  ) = ResponseEntity.ok(messageRepository.getEventMessages(event_id, PageRequest(page, pageLimit)))

  @MessageMapping("/chat.private.{username}")
  @SendTo("/user/{username}/queue/chat.message")
  fun sendDirectMessage(
    @Payload message: Message,
    @DestinationVariable("username") username: String,
    principal: Principal
  ): Message {
    val recipient = userRepository.findByEmail(username)

    message.apply {
      sender = principal as User;
      recipientType = RecipientType.USER;
      recipientId = recipient.id;
    }

    return messageRepository.save(message)
  }

  @MessageMapping("/chat.page.{page_id}")
  @SendTo("/topic/page/{page_id}/chat")
  fun sendMessageToPage(
    @Payload message: Message,
    @DestinationVariable("page_id") page_id: Long?,
    principal: Principal
  ): Message {
    message.apply {
      sender = principal as User;
      recipientType = RecipientType.PAGE;
      recipientId = page_id;
    }
    return messageRepository.save(message)
  }

  @MessageMapping("/chat.event.{event_id}")
  @SendTo("/topic/event/{event_id}/chat")
  fun sendMessageToEvent(
    @Payload message: Message,
    @DestinationVariable("event_id") event_id: Long?,
    principal: Principal
  ): Message {
    message.apply {
      sender = principal as User;
      recipientType = RecipientType.EVENT;
      recipientId = event_id;
    }
    return messageRepository.save(message)
  }

//  @MessageMapping("/chat.{recipient_type}.{recipient_id}")
//  @SendTo("/topic/{recipient_type}/{recipient_id}/chat")
//  fun sendMessageToRecipient(
//    @Payload message: Message,
//    @DestinationVariable("recipient_id") recipient_id: Long?,
//    @DestinationVariable("recipient_type") recipient_type: RecipientType,
//    principal: Principal
//  ): Message {
//    message.apply {
//      sender = principal as User
//      recipientType = recipient_type
//      recipientId = recipient_id
//    }
//
//    return messageRepository.save(message)
//  }
}
