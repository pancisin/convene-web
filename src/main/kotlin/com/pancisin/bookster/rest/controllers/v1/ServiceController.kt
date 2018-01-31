package com.pancisin.bookster.rest.controllers.v1

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.pancisin.bookster.services.EmailService
import com.pancisin.bookster.services.NotificationService
import com.pancisin.bookster.model.BookRequest
import com.pancisin.bookster.model.Service
import com.pancisin.bookster.model.User
import com.pancisin.bookster.repository.BookRequestRepository
import com.pancisin.bookster.repository.ServiceRepository

@RestController
@RequestMapping("/api/service/{service_id}")
class ServiceController {

  @Autowired
  lateinit var serviceRepository: ServiceRepository

  @Autowired
  lateinit var bookRequestRepository: BookRequestRepository

  @Autowired
  lateinit var emailService: EmailService

  @Autowired
  lateinit var notificationService: NotificationService

  @GetMapping
  @PreAuthorize("hasPermission(#service_id, 'service', 'read')")
  fun getService(@PathVariable service_id: Long?) = ResponseEntity.ok(serviceRepository.findOne(service_id))

  @DeleteMapping
  @PreAuthorize("hasPermission(#service_id, 'service', 'update')")
  fun deleteService(@PathVariable service_id: Long?): ResponseEntity<*> {
    serviceRepository.delete(service_id)
    return ResponseEntity.ok("success")
  }

  @PutMapping
  @PreAuthorize("hasPermission(#service_id, 'service', 'update')")
  fun putService(@PathVariable service_id: Long?, @RequestBody service: Service): ResponseEntity<Service> {
    val stored = serviceRepository.findOne(service_id).apply {
      name = service.name
      detail = service.detail
      price = service.price
      unit = service.unit
      form = service.form
    }
    return ResponseEntity.ok(serviceRepository.save(stored))
  }

  @PostMapping("/request")
  @PreAuthorize("hasPermission(#service_id, 'service', 'read')")
  fun postRequest(@PathVariable service_id: Long?, @RequestBody request: BookRequest): ResponseEntity<*> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    val service = serviceRepository.findOne(service_id)

    request.apply {
      user = auth
      this.service = service
      approved = false
    }

    val message = ("User " + auth.username + " requested " + request.units + " units of " + service.name)
    service.page?.administrators?.forEach { (_, user) ->
      if (user != null) {
        emailService.sendSimpleMessage(user.email.toString(), "Service request", message)
        notificationService.notifyUser(user, "Service request", message)
      }
    }

    return ResponseEntity.ok(bookRequestRepository.save(request))
  }

  @GetMapping("/request")
  @PreAuthorize("hasPermission(#service_id, 'service', 'read')")
  fun getRequests(@PathVariable service_id: Long?) = serviceRepository.findOne(service_id).requests;
}
