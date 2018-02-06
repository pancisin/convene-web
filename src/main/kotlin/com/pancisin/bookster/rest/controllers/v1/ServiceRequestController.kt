package com.pancisin.bookster.rest.controllers.v1

import com.pancisin.bookster.model.ServiceRequest
import com.pancisin.bookster.repository.ServiceRequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/service-request/{request_id}")
class ServiceRequestController {

  @Autowired
  lateinit var serviceRequestRepository: ServiceRequestRepository

  @PatchMapping("/accept")
  fun acceptRequest(@PathVariable request_id: Long): ResponseEntity<ServiceRequest> {
    val stored = serviceRequestRepository.findOne(request_id).apply {
      approved = true
    }
    return ResponseEntity.ok(serviceRequestRepository.save(stored))
  }

  @PatchMapping("/refuse")
  fun refuseRequest(@PathVariable request_id: Long): ResponseEntity<ServiceRequest> {
    val stored = serviceRequestRepository.findOne(request_id).apply {
      approved = false
    }
    return ResponseEntity.ok(serviceRequestRepository.save(stored))
  }
}
