package com.pancisin.bookster.rest.controllers.v1

import com.pancisin.bookster.components.annotations.ActivityLog

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.pancisin.bookster.components.storage.StorageServiceImpl
import com.pancisin.bookster.events.OnInviteEvent
import com.pancisin.bookster.model.*
import com.pancisin.bookster.model.enums.ActivityType
import com.pancisin.bookster.model.enums.PageState
import com.pancisin.bookster.repository.*
import com.pancisin.bookster.exceptions.InvalidRequestException
import com.pancisin.bookster.exceptions.ResourceNotFoundException
import com.pancisin.bookster.exceptions.UnallowedRequestException
import org.springframework.data.domain.Page
import java.util.*

@RestController
@RequestMapping("/api/v1/event/{event_id}")
class EventController {

  @Autowired
  lateinit var eventRepository: EventRepository

  @Autowired
  lateinit var programmeRepository: ProgrammeRepository

  @Autowired
  lateinit var storageService: StorageServiceImpl

  @Autowired
  lateinit var invitationRepository: InvitationRepository

  @Autowired
  lateinit var userRepository: UserRepository

  @Autowired
  lateinit var eventPublisher: ApplicationEventPublisher

  @Autowired
  lateinit var mediaRepository: MediaRepository

  @Autowired
  lateinit var ratingRepository: RatingRepository

  @GetMapping
  @PreAuthorize("hasPermission(#event_id, 'event', 'read')")
  fun getEvent(@PathVariable event_id: Long?): ResponseEntity<Event> {
    val event = eventRepository.findOne(event_id)

    event?.let {
      return  ResponseEntity.ok(event);
    }

    throw ResourceNotFoundException("Event identified with ${event_id} can't be found!")
  }

  @PutMapping
  @ActivityLog(type = ActivityType.UPDATE)
  @PreAuthorize("hasPermission(#event_id, 'event', 'update')")
  fun putEvent(
    @PathVariable event_id: Long?,
    @Valid @RequestBody event: Event,
    bindingResult: BindingResult
  ): ResponseEntity<*> {
    if (bindingResult.hasErrors())
      throw InvalidRequestException("Invalid data", bindingResult)

    val stored = eventRepository.findOne(event_id).apply {
      name = event.name
      summary = event.summary
      visibility = event.visibility
      date = event.date
      place = event.place
      latitude = event.latitude
      longitude = event.longitude
      banner = event.banner
      tags = event.tags
    }

    if (event.posterData != null && storageService.isBinary(event.posterData)) {
      val poster = mediaRepository.save(Media(author = SecurityContextHolder.getContext().authentication.principal as User))
      val url = "banners/events/" + poster.id.toString()

      stored.poster = poster.apply {
        path = "/files/$url.jpg"
        size = storageService.storeBinary(event.posterData, url)
      }
    }

    return ResponseEntity.ok(eventRepository.save(stored))
  }

  @DeleteMapping
  @ActivityLog(type = ActivityType.DELETE)
  @PreAuthorize("hasPermission(#event_id, 'event', 'update')")
  fun deleteEvent(@PathVariable event_id: Long?): ResponseEntity<*> {
    eventRepository.delete(event_id)
    return ResponseEntity.ok("success")
  }

  @GetMapping("/programme")
  fun getProgramme(@PathVariable event_id: Long?) = ResponseEntity.ok(eventRepository.findOne(event_id).programme)

  @PostMapping("/programme")
  @ActivityLog(type = ActivityType.CREATE_PROGRAMME)
  @PreAuthorize("hasPermission(#event_id, 'event', 'update')")
  fun postProgramme(@PathVariable event_id: Long?, @RequestBody programme: Programme) = ResponseEntity.ok(programmeRepository.save(programme.apply {
    event = eventRepository.findOne(event_id)
  }))


  @PatchMapping("/toggle-attend")
  @ActivityLog(type = ActivityType.ATTENDING)
  @PreAuthorize("hasPermission(#event_id, 'event', 'read')")
  fun toggleAttend(@PathVariable event_id: Long?): ResponseEntity<*> {
    val stored = eventRepository.findOne(event_id)

    stored.date?.let {
      if (it.before(Calendar.getInstance())) {
        throw UnallowedRequestException("error.event.ended")
      }
    }

    val auth_user = SecurityContextHolder.getContext().authentication.principal as User
    val attending = eventRepository.isAttending(event_id, auth_user.id) > 0

    if (attending) {
      stored.attendees = stored.attendees.filter { it.id !== auth_user.id }.toMutableList()
    } else {
      stored.attendees.add(auth_user)
    }

    eventRepository.save(stored)
    return ResponseEntity.ok(!attending)
  }

  @GetMapping("/attend-status")
  @PreAuthorize("hasPermission(#event_id, 'event', 'read')")
  fun getAttendStatus(@PathVariable event_id: Long?): ResponseEntity<*> {
    val auth_user = SecurityContextHolder.getContext().authentication.principal as User
    val attend_count = eventRepository.isAttending(event_id, auth_user.id)
    return ResponseEntity.ok(attend_count > 0)
  }

  @GetMapping("/attendees")
  @PreAuthorize("hasPermission(#event_id, 'event', 'read')")
  fun getAttendees(@PathVariable event_id: Long?) = ResponseEntity.ok(eventRepository.findOne(event_id).attendees)

  @PostMapping("/invite")
  @PreAuthorize("hasPermission(#event_id, 'event', 'update')")
  fun postInvitation(@PathVariable event_id: Long?, @RequestBody invitation: Invitation): ResponseEntity<*> {
    val stored = eventRepository.findOne(event_id)
    var inv = Invitation(
      event = stored,
      email = invitation.email.toString(),
      user = userRepository.findByEmail(invitation.email)
    )

    inv = invitationRepository.save(inv)
    eventPublisher.publishEvent(OnInviteEvent(inv))
    return ResponseEntity.ok(inv)
  }

  @GetMapping("/invitation")
  @PreAuthorize("hasPermission(#event_id, 'event', 'update')")
  fun getInvitations(@PathVariable event_id: Long?) = ResponseEntity.ok(eventRepository.findOne(event_id).invitations)

  @GetMapping("/related")
  fun getRelatedEvents(@PathVariable event_id: Long?) = ResponseEntity.ok<Page<Event>>(eventRepository.getRelated(event_id, PageRequest(0, 100)))

  @GetMapping("/gallery")
  fun getGallery(@PathVariable event_id: Long?) = ResponseEntity.ok(mediaRepository.getByEvent(event_id))

  @PostMapping("/gallery")
  fun postGallery(@PathVariable event_id: Long?, @RequestBody galleryItem: Media): ResponseEntity<*> {
    val stored = eventRepository.findOne(event_id)

    if (storageService.isBinary(galleryItem.data)) {
      val user = SecurityContextHolder.getContext().authentication.principal as User

      mediaRepository.save(galleryItem.apply {
        author = user
      })

      val url = "images/event/" + galleryItem.id.toString()
      stored.addGallery(galleryItem.apply {
        path = "/files/$url.jpg"
        size = storageService.storeBinary(galleryItem.data, url)
      })
    }

    eventRepository.save(stored)
    return ResponseEntity.ok(galleryItem)
  }

  @PatchMapping("/toggle-published")
  @ActivityLog(type = ActivityType.PUBLISH)
  @PreAuthorize("hasPermission(#event_id, 'event', 'update')")
  fun togglePublishState(@PathVariable event_id: Long?): ResponseEntity<*> {
    val stored = eventRepository.findOne(event_id)

    if (stored.state === PageState.DEACTIVATED) {
      stored.state = PageState.PUBLISHED
    } else if (stored.state === PageState.PUBLISHED) {
      stored.state = PageState.DEACTIVATED
    }

    return ResponseEntity.ok(eventRepository.save(stored))
  }

  @PatchMapping("/toggle-featured")
  @ActivityLog(type = ActivityType.FEATURE)
  @PreAuthorize("hasRole('SUPERADMIN')")
  fun toggleFeatured(@PathVariable event_id: Long?)
    = ResponseEntity.ok(eventRepository.save(eventRepository.findOne(event_id).apply {
    featured = !featured
  }))

  @GetMapping("/rating/{page}/{size}")
  fun getRating(
    @PathVariable event_id: Long,
    @PathVariable page: Int,
    @PathVariable size: Int
  ) = ResponseEntity.ok(ratingRepository.getForEvent(event_id, PageRequest(page, size)))

  @PostMapping("/rating")
  fun postRating(
    @PathVariable event_id: Long,
    @RequestBody rating: Rating
  ) : ResponseEntity<Rating> {
    val stored = eventRepository.findOne(event_id)

    rating.apply {
      event = stored
      user = SecurityContextHolder.getContext().authentication.principal as User
    }

    if (!stored.attendees.any { it.id === rating.user?.id }) {
      throw UnallowedRequestException("error.rating.not_attending")
    }

    stored.date?.let {
      if (it.after(Calendar.getInstance())) {
        throw UnallowedRequestException("error.rating.premature_rating")
      }
    }

    return ResponseEntity.ok(ratingRepository.save(rating))
  }
}
