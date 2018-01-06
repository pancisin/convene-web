package com.pancisin.bookster.rest.controllers.v1

import com.pancisin.bookster.model.Invitation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.pancisin.bookster.repository.InvitationRepository

@RestController
@PreAuthorize("hasPermission(#invitation_id, 'invitation', '')")
@RequestMapping("/api/v1/invitation/{invitation_id}")
class InvitationController {

  @Autowired
  lateinit var invitationRepository: InvitationRepository

  @GetMapping
  fun getInvitation(@PathVariable invitation_id: Long?) = ResponseEntity.ok(invitationRepository!!.findOne(invitation_id))

  @DeleteMapping
  fun deleteInvitation(@PathVariable invitation_id: Long?): ResponseEntity<String> {
    invitationRepository.delete(invitation_id)
    return ResponseEntity.ok("success")
  }
}
