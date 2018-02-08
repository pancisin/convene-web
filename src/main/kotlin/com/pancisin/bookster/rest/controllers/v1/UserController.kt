package com.pancisin.bookster.rest.controllers.v1

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.fasterxml.jackson.annotation.JsonView
import com.pancisin.bookster.models.views.Summary
import com.pancisin.bookster.repository.EventRepository
import com.pancisin.bookster.repository.PageRepository
import com.pancisin.bookster.repository.UserRepository

@RestController
@RequestMapping("api/v1/user/{user_id}")
class UserController {

  @Autowired
  lateinit var userRepository: UserRepository

  @Autowired
  lateinit var pageRepository: PageRepository

  @Autowired
  lateinit var eventRepository: EventRepository

  @JsonView(Summary::class)
  @GetMapping("/page")
  fun getPage(@PathVariable user_id: Long) = ResponseEntity.ok(pageRepository.getByOwner(user_id))

  @GetMapping("/followed-pages")
  @JsonView(Summary::class)
  fun getFollowedPages(@PathVariable user_id: Long) = ResponseEntity.ok(pageRepository.getFollowed(user_id))

  @GetMapping("/privacy-constraints")
  fun getPrivacyConstraints(@PathVariable user_id: Long) = ResponseEntity.ok(userRepository.findOne(user_id).privacyConstraints)
}
