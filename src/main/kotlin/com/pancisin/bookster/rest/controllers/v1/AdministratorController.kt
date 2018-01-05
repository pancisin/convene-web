package com.pancisin.bookster.rest.controllers.v1

import com.pancisin.bookster.model.Administrator
import com.pancisin.bookster.repository.AdministratorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/administrator/{id}")
@PreAuthorize("hasPermission(#id, 'administrator', 'update')")
class AdministratorController {

  @Autowired
  lateinit var administratorRepository: AdministratorRepository

  @GetMapping
  fun getAdministrator(@PathVariable id: Long) = ResponseEntity.ok(administratorRepository.findOne(id))

  @PutMapping
  fun putAdministrator(@PathVariable id: Long, @RequestBody administrator: Administrator): ResponseEntity<*> {
    return ResponseEntity.ok(administratorRepository.save(administratorRepository.findOne(id).copy(role = administrator.role, active = administrator.active)))
  }

  @DeleteMapping
  fun deleteAdministrator(@PathVariable id: Long): ResponseEntity<*> {
    administratorRepository.delete(id)
    return ResponseEntity.ok("success")
  }
}
