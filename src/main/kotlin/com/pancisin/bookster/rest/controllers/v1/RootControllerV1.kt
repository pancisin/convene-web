package com.pancisin.bookster.rest.controllers.v1

import com.pancisin.bookster.components.annotations.PrivacyRestricted
import com.pancisin.bookster.exceptions.ResourceNotFoundException
import com.pancisin.bookster.model.*
import com.pancisin.bookster.model.dtos.UserDto
import com.pancisin.bookster.repository.*
import com.pancisin.bookster.repository.custom.EventSearchRepository
import com.pancisin.bookster.repository.custom.UserSearchRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.util.*

@RestController
class RootControllerV1 {

  @Autowired
  lateinit var alRepository: ArticlesListRepository

  @Autowired
  lateinit var articleRepository: ArticleRepository

  @Autowired
  lateinit var pageRepository: PageRepository

  @Autowired
  lateinit var eventRepository: EventRepository

  @Autowired
  lateinit var conferenceRepository: ConferenceRepository

  @Autowired
  lateinit var userRepository: UserRepository

  @Autowired
  lateinit var userSearchRepository: UserSearchRepository

  @Autowired
  lateinit var eventSearchRepository: EventSearchRepository

  @GetMapping("/api/v1/articles", "/public/v1/articles")
  fun getArticles(
    @RequestParam(required = false, name = "tags", defaultValue = "language:en,headlines") tags: List<String>?,
    @RequestParam(required = false, defaultValue = "0") page: Int,
    @RequestParam(required = false, defaultValue = "10") size: Int): ResponseEntity<Page<Article>>? {

    val tagsHash = tags?.sorted()?.hashCode() ?: 0
    val articlesList: ArticlesList? = alRepository.findByTagsHash(tagsHash)

    articlesList?.let {
      val articles = articleRepository.getByArticlesList(articlesList.id, PageRequest(page, size, Direction.DESC, "created"))
      return ResponseEntity.ok(articles)
    }

    return ResponseEntity(HttpStatus.BAD_REQUEST)
  }

  @GetMapping("/api/v1/pages/{page}/{limit}", "/public/v1/pages/{page}/{limit}")
  fun getPages(
    @PathVariable page: Int, @PathVariable limit: Int,
    @RequestParam(name = "categoryId", required = false) categoryId: Long?,
    @RequestParam(name = "branchId", required = false) branchId: Long?): ResponseEntity<*> {

    val pages: Page<com.pancisin.bookster.model.Page>?
    val pageable = PageRequest(page, limit, Sort(Direction.ASC, "name"))

    if (branchId != null) {
      pages = pageRepository.findByBranch(branchId, pageable)
    } else if (categoryId != null) {
      pages = pageRepository.findByCategory(categoryId, pageable)
    } else {
      pages = pageRepository.findAllVisible(pageable)
    }

    return ResponseEntity.ok(pages)
  }

  @GetMapping("/api/v1/events/{page}/{limit}", "/public/v1/events/{page}/{limit}")
  fun getEvents(
    @PathVariable page: Int, @PathVariable limit: Int,
    @RequestParam(name = "timestamp", required = false) timestamp: Long,
    @RequestParam(name = "authorType", required = false, defaultValue = "") authorType: String,
    @RequestParam(name = "authorId", required = false, defaultValue = "0") authorId: String): ResponseEntity<*>? {

    val principal = SecurityContextHolder.getContext().authentication.principal
    val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
    val pageable = PageRequest(page, limit, Sort(Direction.ASC, "date"))

    when (authorType) {
      "PAGE" ->
        // TODO
        return null
      "USER" -> {
        val userId = java.lang.Long.parseLong(authorId)
        return ResponseEntity.ok(eventRepository.getPublicCreatedByUser(userId, cal, pageable))
      }
      "CONFERENCE" ->
        // TODO
        return null
      else -> if (principal is String) {
        return ResponseEntity.ok(eventRepository.getPublicByDate(cal, pageable))
      } else {
        val id = (principal as User).id
        return ResponseEntity.ok(eventRepository.getForUserByDate(id, cal, pageable))
      }
    }
  }

  @GetMapping("/api/v1/conferences/{page}/{size}", "/public/v1/conferences/{page}/{size}")
  fun getConferences(@PathVariable page: Int, @PathVariable size: Int) : ResponseEntity<*> {
    val auth = SecurityContextHolder.getContext().authentication.principal as? User

    auth?.let {
      return ResponseEntity.ok(conferenceRepository.getForUser(auth.id, PageRequest(page, size)))
    }

    return ResponseEntity.ok(conferenceRepository.findPublic(PageRequest(page, size)))
  }

  @GetMapping("/api/v1/page/{page_identifier}", "/public/v1/page/{page_identifier}")
  @PreAuthorize("hasPermission(#page_identifier, 'page', 'read')")
  fun getPage(
    @PathVariable page_identifier: String
  ): ResponseEntity<com.pancisin.bookster.model.Page> {

    page_identifier.toLongOrNull()?.let { id ->
      pageRepository.findOne(id)?.let { return ResponseEntity.ok(it) }
    }

    pageRepository.findBySlug(page_identifier)?.let { return ResponseEntity.ok(it) }
    throw ResourceNotFoundException("Page identified with ${page_identifier} can't be found!")
  }

  @GetMapping("/api/v1/conference/{conference_identifier}", "/public/v1/conference/{conference_identifier}")
  fun getConference(
    @PathVariable conference_identifier: String
  ): ResponseEntity<com.pancisin.bookster.model.Page> {

    conference_identifier.toLongOrNull()?.let {
      conferenceRepository.findOne(it)?.let { return ResponseEntity.ok(it) }
    }

    conferenceRepository.findBySlug(conference_identifier)?.let { return ResponseEntity.ok(it) }
    throw ResourceNotFoundException("Conference identified with ${conference_identifier} can't be found!")
  }

  @GetMapping("/api/v1/featured-events/{page}/{size}", "/public/v1/featured-events/{page}/{size}")
  fun getFeaturedEvents(
    @PathVariable page: Int, @PathVariable size: Int,
    @RequestParam(name = "fromDate", required = false) fromDateTimestamp: Long,
    @RequestParam(name = "toDate", required = false) toDateTimestamp: Long
  ) = ResponseEntity.ok(eventRepository.getFeaturedEvents(
    Calendar.getInstance().apply { timeInMillis = fromDateTimestamp },
    Calendar.getInstance().apply { timeInMillis = toDateTimestamp },
    PageRequest(page, size, Direction.ASC, "date"))
  )

  @GetMapping("/api/v1/near-events/{page}/{size}", "/public/v1/near-events/{page}/{size}")
  fun getNearEvents(
    @PathVariable page: Int,
    @PathVariable size: Int,
    @RequestParam(name = "lat") lat: BigDecimal,
    @RequestParam(name = "lng") lng: BigDecimal,
    @RequestParam(name = "distance") distance: Double?,
    @RequestParam(name = "fromDate", required = false) fromDateTimestamp: Long,
    @RequestParam(name = "toDate", required = false) toDateTimestamp: Long
  ) : ResponseEntity<Page<Event>> {

    val fromDate = Calendar.getInstance().apply { timeInMillis = fromDateTimestamp }
    val toDate = Calendar.getInstance().apply { timeInMillis = toDateTimestamp }

    return ResponseEntity.ok(eventRepository.getEventsByDistanceFrom(lat, lng, distance, fromDate, toDate, PageRequest(page, size, Sort(Direction.ASC, "date"))))
  }

  @PrivacyRestricted(constraint = "profile")
  @GetMapping("/api/v1/user/{user_id}", "/public/v1/user/{user_id}")
  fun getUser(@PathVariable user_id: Long): ResponseEntity<User> {
    userRepository.findOne(user_id)?.let {
      return ResponseEntity.ok(it)
    }
    throw ResourceNotFoundException("Profile identified with ${user_id} can't be found!")
  }

  @PrivacyRestricted(constraint = "events")
  @GetMapping("/api/v1/user/{user_id}/event/{page}/{size}", "/public/v1/user/{user_id}/event/{page}/{size}")
  fun getUserEvents(
    @PathVariable user_id: Long?,
    @PathVariable page: Int,
    @PathVariable size: Int
  ) = ResponseEntity.ok(eventRepository.getByUser(user_id, PageRequest(page, size, Sort(Direction.ASC, "date"))))

  @PrivacyRestricted(constraint = "attending-events")
  @GetMapping("/api/v1/user/{user_id}/attending-events", "/public/v1/user/{user_id}/attending-events")
  fun getAttendingEvents(@PathVariable user_id: Long) = ResponseEntity.ok(eventRepository.getAttending(user_id))

  @GetMapping("/api/v1/user/{user_id}/privacy-constraints", "/public/v1/user/{user_id}/privacy-constraints")
  fun getUserPrivacyConstraints(@PathVariable user_id: Long) = ResponseEntity.ok(userRepository.findOne(user_id).privacyConstraints)

  @GetMapping("/api/v1/user/search", "/public/v1/user/search")
  fun searchUsers(@RequestParam q: String) = ResponseEntity.ok(userSearchRepository.search(q).filterIsInstance<User>().map { UserDto.fromUserModel(it) })

  @GetMapping("/api/v1/event/search", "/public/v1/event/search")
  fun searchEvents(@RequestParam q: String) = ResponseEntity.ok(eventSearchRepository.search(q).filterIsInstance<Event>())
}
