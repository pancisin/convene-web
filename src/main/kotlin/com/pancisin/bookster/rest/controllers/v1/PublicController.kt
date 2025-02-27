package com.pancisin.bookster.rest.controllers.v1

import com.pancisin.bookster.exceptions.ResourceNotFoundException
import com.pancisin.bookster.model.*
import com.pancisin.bookster.model.enums.*
import com.pancisin.bookster.model.enums.Unit
import com.pancisin.bookster.repository.*

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
@RequestMapping("/public/v1")
class PublicController {

  @Autowired
  lateinit var eventRepository: EventRepository

  @Autowired
  lateinit var pageRepository: PageRepository

  @Autowired
  lateinit var categoryRepository: CategoryRepository

  @Autowired
  lateinit var branchRepository: BranchRepository

  @Autowired
  lateinit var articleRepository: ArticleRepository

  @Autowired
  lateinit var mediaRepository: MediaRepository

  @Autowired
  lateinit var userRepository: UserRepository

  @Autowired
  lateinit var conferenceRepository: ConferenceRepository

  @GetMapping("/locales")
  fun getLocales() = ResponseEntity.ok(Locale.values())

  @GetMapping("/subscriptions")
  fun getSubscriptions() = ResponseEntity.ok(Subscription.values())

  @GetMapping("/meta-types")
  fun getMetaTypes() = ResponseEntity.ok(FieldType.values())

  @GetMapping("/event/{event_id}")
  fun getEvent(@PathVariable event_id: Long): ResponseEntity<Event> {
    val event = eventRepository.findOne(event_id) ?: throw ResourceNotFoundException("Event identified with ${event_id} can't be found!")

    return if (event.visibility === Visibility.PUBLIC && event.state === PageState.PUBLISHED)
      ResponseEntity.ok(event)
    else
      ResponseEntity(HttpStatus.FORBIDDEN)
  }

  @GetMapping("/event/{event_id}/related")
  fun getRelatedEvents(@PathVariable event_id: Long) = ResponseEntity.ok(eventRepository.getRelated(event_id, PageRequest(0, 100)))

  @GetMapping("/event/{event_id}/gallery")
  fun getEventGallery(@PathVariable event_id: Long) = ResponseEntity.ok(mediaRepository.getByEvent(event_id))

  @GetMapping("/event/{event_id}/attendees")
  fun getAttendees(@PathVariable event_id: Long) = ResponseEntity.ok(eventRepository.findOne(event_id).attendees)

  @GetMapping("/popular-pages/{page}/{limit}")
  fun getPopularPages(@PathVariable page: Int, @PathVariable limit: Int) = ResponseEntity.ok(pageRepository.getPopular(PageRequest(page, limit)))

  @GetMapping("/page/{page_id}/service")
  fun getPageServices(@PathVariable page_id: Long) = ResponseEntity.ok(pageRepository.findOne(page_id).services)

  @GetMapping("/page/{page_id}/event/{page}/{size}")
  fun getEvents(
    @PathVariable page_id: Long,
    @PathVariable page: Int,
    @PathVariable size: Int,
    @RequestParam("fromDate") fromDate: String,
    @RequestParam("toDate") toDate: String
  ) = ResponseEntity.ok(eventRepository.getByPageRange(page_id, PageRequest(page, size, Sort(Direction.ASC, "date")), fromDate, toDate, null))

  @GetMapping("/categories")
  fun getCategories(@RequestParam(name = "used", defaultValue = "true") used: Boolean): ResponseEntity<MutableList<Category>>? {
    return if (used) ResponseEntity.ok(categoryRepository.used) else ResponseEntity.ok(categoryRepository.findAll())
  }

  @GetMapping("/categories/{category_id}/branches")
  fun getBraches(@PathVariable category_id: Long, @RequestParam(name = "used", defaultValue = "true") used: Boolean): ResponseEntity<List<Branch>>? {
    return if (used) ResponseEntity.ok(branchRepository.getUsed(category_id)) else ResponseEntity.ok(categoryRepository.findOne(category_id).branches)
  }

  @GetMapping("/conference/{conference_id}/event")
  fun getConferenceEvents(@PathVariable conference_id: Long) = ResponseEntity.ok(eventRepository.getByConference(conference_id, PageRequest(0, 10)))

  @GetMapping("/conference/{conference_id}/article/{page}/{size}")
  fun getConferenceArticles(
    @PathVariable conference_id: Long,
    @PathVariable page: Int,
    @PathVariable size: Int
  ) = ResponseEntity.ok(articleRepository.getByPage(conference_id, PageRequest(page, size, Direction.DESC, "created")))

  @GetMapping("/conference/{page}/{size}")
  fun getConferences(@PathVariable page: Int, @PathVariable size: Int) = ResponseEntity.ok(conferenceRepository.findPublic(PageRequest(page, size)))

  @GetMapping("/unit")
  fun getUnits() = ResponseEntity.ok(Unit.values())

  @GetMapping("/roles")
  fun getRoles() = ResponseEntity.ok(PageRole.values())
}
