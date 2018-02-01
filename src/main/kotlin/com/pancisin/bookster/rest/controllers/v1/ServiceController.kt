package com.pancisin.bookster.rest.controllers.v1

import com.pancisin.bookster.model.*
import com.pancisin.bookster.repository.FormSubmissionRepository
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
import com.pancisin.bookster.repository.ServiceRequestRepository
import com.pancisin.bookster.repository.ServiceRepository
import org.springframework.http.HttpStatus
import javax.transaction.Transactional

@RestController
@RequestMapping("/api/service/{service_id}")
class ServiceController {

  @Autowired
  lateinit var serviceRepository: ServiceRepository

  @Autowired
  lateinit var serviceRequestRepository: ServiceRequestRepository

  @Autowired
  lateinit var emailService: EmailService

  @Autowired
  lateinit var notificationService: NotificationService

  @Autowired
  lateinit var formSubmissionRepository: FormSubmissionRepository

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
  @Transactional
  @PreAuthorize("hasPermission(#service_id, 'service', 'read')")
  fun postRequest(
    @PathVariable service_id: Long,
    @RequestBody values: MutableList<FormFieldValue>
  ) : ResponseEntity<ServiceRequest> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    val service = serviceRepository.findOne(service_id)

    var submission = FormSubmission(
      user = auth,
      values = values,
      form = service.form
    )
    submission = formSubmissionRepository.save(submission)

    return ResponseEntity.ok(serviceRequestRepository.save(ServiceRequest(
      submission = submission,
      user = auth,
      service = service
    )))
  }

  @GetMapping("/request")
  @PreAuthorize("hasPermission(#service_id, 'service', 'read')")
  fun getRequests(@PathVariable service_id: Long): ResponseEntity<List<ServiceRequest>> {
    val service = serviceRepository.findOne(service_id)

    if (!service.requestPending) {
      return ResponseEntity.ok(service.requests);
    } else {
      return ResponseEntity(HttpStatus.FORBIDDEN);
    }
  }
}
