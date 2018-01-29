package com.pancisin.bookster.rest.controllers.v1

import com.fasterxml.jackson.annotation.JsonView
import com.pancisin.bookster.components.annotations.ActivityLog
import com.pancisin.bookster.components.storage.StorageServiceImpl
import com.pancisin.bookster.events.OnInviteEvent
import com.pancisin.bookster.model.*
import com.pancisin.bookster.model.enums.ActivityType
import com.pancisin.bookster.model.enums.PageRole
import com.pancisin.bookster.model.enums.PageState
import com.pancisin.bookster.models.views.Summary
import com.pancisin.bookster.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

import javax.transaction.Transactional

@RestController
@RequestMapping("/api/v1/conference/{conference_id}")
class ConferenceController {

  @Autowired
  lateinit var conferenceRepository: ConferenceRepository

  @Autowired
  lateinit var eventRepository: EventRepository

  @Autowired
  lateinit var userRepository: UserRepository

  @Autowired
  lateinit var invitationRepository: InvitationRepository

  @Autowired
  lateinit var eventPublisher: ApplicationEventPublisher

  @Autowired
  lateinit var storageService: StorageServiceImpl

  @Autowired
  lateinit var articleRepository: ArticleRepository

  @Autowired
  lateinit var surveyRepository: SurveyRepository

  @Autowired
  lateinit var placeRepository: PlaceRepository

  @Autowired
  lateinit var mediaRepository: MediaRepository

  @Autowired
  lateinit var administratorRepository: AdministratorRepository

  @Autowired
  lateinit var pageMemberRepository: PageMemberRepository

  @Autowired
  lateinit var activityRepository: ActivityRepository

  @GetMapping
  @PreAuthorize("hasPermission(#conference_id, 'page', 'read')")
  fun getConference(@PathVariable conference_id: Long): ResponseEntity<Page> {
    val conference = conferenceRepository.findOne(conference_id)

    return if (conference == null) {
      ResponseEntity(HttpStatus.NOT_FOUND)
    } else {
      ResponseEntity.ok(conference)
    }
  }

  @PutMapping
  @ActivityLog(type = ActivityType.UPDATE)
  @PreAuthorize("hasPermission(#conference_id, 'page', 'update')")
  fun putConference(@PathVariable conference_id: Long?, @RequestBody conference: Page): ResponseEntity<Page> {
    val stored = conferenceRepository.findOne(conference_id).apply {
      name = conference.name
      branch = conference.branch
      summary = conference.summary
      visibility = conference.visibility
    }

    if (conference.posterData != null && storageService.isBinary(conference.posterData)) {
      val poster = mediaRepository.save(Media(author = SecurityContextHolder.getContext().authentication.principal as User))
      val url = "banners/conferences/" + poster.id.toString()

      stored.poster = poster.apply {
        path = "/files/$url.jpg"
        size = storageService.storeBinary(conference.posterData, url)
      }
    }
    return ResponseEntity.ok(conferenceRepository.save(stored))
  }

  @DeleteMapping
  @ActivityLog(type = ActivityType.DELETE)
  @PreAuthorize("hasPermission(#conference_id, 'page', 'update')")
  fun deleteConference(@PathVariable conference_id: Long?): ResponseEntity<Page> {
    val stored = conferenceRepository.findOne(conference_id).apply {
      state = PageState.DELETED
    }
    return ResponseEntity.ok(conferenceRepository.save(stored))
  }

  @GetMapping("/event/{page}/{size}")
  @PreAuthorize("hasPermission(#conference_id, 'page', 'read')")
  fun getEvents(
    @PathVariable conference_id: Long?,
    @PathVariable page: Int,
    @PathVariable size: Int
  ) = ResponseEntity.ok(eventRepository.getByPage(conference_id, PageRequest(page, size, Sort(Direction.ASC, "date"))))

  @PostMapping("/event")
  @ActivityLog(type = ActivityType.CREATE_EVENT)
  @PreAuthorize("hasPermission(#conference_id, 'page', 'update')")
  fun postEvent(@PathVariable conference_id: Long?, @RequestBody event: Event): ResponseEntity<*> {
    val stored = conferenceRepository.findOne(conference_id)
    event.page = stored
    return ResponseEntity.ok(eventRepository.save(event))
  }

  @GetMapping("/attendees")
  // @JsonView(Summary.class)
  @PreAuthorize("hasPermission(#conference_id, 'page', 'admin-read')")
  fun getAttendees(@PathVariable conference_id: Long?) = ResponseEntity.ok(pageMemberRepository.findByPage(conference_id))

  @PostMapping("/attend")
  @Transactional
  @ActivityLog(type = ActivityType.ATTENDING)
  @PreAuthorize("hasPermission(#conference_id, 'page', 'read')")
  fun postAttend(@PathVariable conference_id: Long?, @RequestBody meta: List<MetaValue>): ResponseEntity<*> {
    val user = SecurityContextHolder.getContext().authentication.principal as User
    val conference = conferenceRepository.findOne(conference_id)

    val attendee = PageMember(user, conference, meta)
    attendee.meta = meta
    pageMemberRepository.save(attendee)
    return ResponseEntity.ok("ACTIVE")
  }

  @PutMapping("/cancel-attend")
  @ActivityLog(type = ActivityType.ATTENDING)
  @PreAuthorize("hasPermission(#conference_id, 'page', 'read')")
  fun cancelAttend(@PathVariable conference_id: Long?): ResponseEntity<*> {
    val user = SecurityContextHolder.getContext().authentication.principal as User
    val attendee = pageMemberRepository.findByAttendance(conference_id, user.id)

    attendee.active = false
    return ResponseEntity.ok(pageMemberRepository.save(attendee))
  }

  @GetMapping("/attend-status")
  @PreAuthorize("hasPermission(#conference_id, 'page', 'read')")
  fun getAttendStatus(@PathVariable conference_id: Long?): ResponseEntity<*> {
    val user = SecurityContextHolder.getContext().authentication.principal as User
    val (_, _, _, _, active) = pageMemberRepository.findByAttendance(conference_id, user.id)
      ?: return ResponseEntity.ok("INACTIVE")

    return ResponseEntity.ok(if (active) "ACTIVE" else "CANCELED")
  }

//  @GetMapping("/meta-field")
//  @PreAuthorize("hasPermission(#conference_id, 'page', 'admin-read')")
//  fun getMetaFields(@PathVariable conference_id: Long?): ResponseEntity<*> {
//    val conference = conferenceRepository.findOne(conference_id)
//    return ResponseEntity.ok<List<MetaField>>(conference.metaFields)
//  }
//
//  @PostMapping("/meta-field")
//  @ActivityLog(type = ActivityType.UPDATE)
//  @PreAuthorize("hasPermission(#conference_id, 'page', 'update')")
//  fun postMetaField(@PathVariable conference_id: Long?, @RequestBody field: MetaField): ResponseEntity<*> {
//    var field = field
//    val conference = conferenceRepository.findOne(conference_id)
//    field = cmfRepository!!.save(field)
//    conference.addMetaField(field)
//    pageRepository.save(conference)
//    return ResponseEntity.ok(field)
//  }

  @PostMapping("/invitation")
  @ActivityLog(type = ActivityType.UPDATE)
  @PreAuthorize("hasPermission(#conference_id, 'page', 'update')")
  fun postInvitation(@PathVariable conference_id: Long?, @RequestBody invitation: Invitation): ResponseEntity<*> {

    val inv = invitationRepository.save(Invitation(
      page = conferenceRepository.findOne(conference_id),
      user = userRepository.findByEmail(invitation.email.toString()),
      email = invitation.email.toString()
    ))

    if (inv != null)
      eventPublisher.publishEvent(OnInviteEvent(inv))

    return ResponseEntity.ok(inv)
  }

  @GetMapping("/invitation")
  @PreAuthorize("hasPermission(#conference_id, 'page', 'admin-read')")
  fun getInvitations(@PathVariable conference_id: Long?) = ResponseEntity.ok(conferenceRepository.findOne(conference_id).invitations)

  @GetMapping("/administrator")
  @JsonView(Summary::class)
  @PreAuthorize("hasPermission(#conference_id, 'page', 'admin-read')")
  fun getAdministrators(@PathVariable conference_id: Long?) = ResponseEntity.ok(conferenceRepository.findOne(conference_id).administrators)

  @PostMapping("/administrator")
  @ActivityLog(type = ActivityType.CREATE_ADMINISTRATOR)
  // @License(value = Subscription.ENTERPRISE, parent = "conference", parentId =
  // "conference_id")
  @PreAuthorize("hasPermission(#conference_id, 'page', 'update')")
  fun postAdministrator(@PathVariable conference_id: Long?, @RequestBody user: User): ResponseEntity<*> {
    val stored = conferenceRepository.findOne(conference_id)

    val existing = userRepository.findByEmail(user.email)
    if (existing != null) {
      val administrator = Administrator(stored, existing, false)
      administrator.role = PageRole.ROLE_ADMINISTRATOR

      administratorRepository.save(administrator)
      return ResponseEntity.ok(administrator)
    }

    return ResponseEntity("User not found by email", HttpStatus.BAD_REQUEST)
  }

  @PostMapping("/article")
  @PreAuthorize("hasPermission(#conference_id, 'page', 'update')")
  @ActivityLog(type = ActivityType.CREATE_ARTICLE)
  fun postArticle(@PathVariable conference_id: Long?, @RequestBody article: Article): ResponseEntity<*> {
    val stored = conferenceRepository.findOne(conference_id)
    article.published = false
    article.page = stored
    return ResponseEntity.ok(articleRepository.save(article))
  }

  @GetMapping("/article/{page}/{size}")
  @PreAuthorize("hasPermission(#conference_id, 'page', 'read')")
  fun getArticles(
    @PathVariable conference_id: Long?,
    @PathVariable page: Int,
    @PathVariable size: Int
  ) = ResponseEntity.ok(articleRepository.getByPage(conference_id, PageRequest(page, size, Direction.DESC, "created")))

  @PatchMapping("/toggle-published")
  @ActivityLog(type = ActivityType.UPDATE)
  @PreAuthorize("hasPermission(#conference_id, 'page', 'update')")
  fun togglePublishState(@PathVariable conference_id: Long?): ResponseEntity<*> {
    val stored = conferenceRepository.findOne(conference_id)

    if (stored.state === PageState.DEACTIVATED) {
      stored.state = PageState.PUBLISHED
    } else if (stored.state === PageState.PUBLISHED) {
      stored.state = PageState.DEACTIVATED
    }

    return ResponseEntity.ok(conferenceRepository.save(stored))
  }

  @Transactional
  @PostMapping("/survey")
  @ActivityLog(type = ActivityType.CREATE_SURVEY)
  @PreAuthorize("hasPermission(#conference_id, 'page', 'update')")
  fun postSurvey(@PathVariable conference_id: Long?, @RequestBody survey: Survey): ResponseEntity<*> {
    val stored = conferenceRepository.findOne(conference_id)
    survey.page = stored
    return ResponseEntity.ok(surveyRepository.save(survey))
  }

  @GetMapping("/survey")
  @PreAuthorize("hasPermission(#conference_id, 'page', 'read')")
  fun getSurveys(
    @PathVariable conference_id: Long?,
    @RequestParam(name = "submitted", required = false, defaultValue = "false") submitted: String
  ): ResponseEntity<*> {
    val stored = conferenceRepository.findOne(conference_id)

    if (java.lang.Boolean.parseBoolean(submitted)) {
      return ResponseEntity.ok<List<Survey>>(stored.surveys)
    } else {
      val user = SecurityContextHolder.getContext().authentication.principal as User
      val surveys = surveyRepository.getByPage(conference_id, user.id)
      return ResponseEntity.ok(surveys)
    }
  }

  @GetMapping("/activity/{page}/{size}")
  @PreAuthorize("hasPermission(#conference_id, 'page', 'admin-read')")
  fun getActivity(
    @PathVariable conference_id: Long?,
    @PathVariable page: Int,
    @PathVariable size: Int
  ) = ResponseEntity.ok(activityRepository.getByPage(conference_id, PageRequest(page, size, Direction.DESC, "created")))

  @GetMapping("/widget")
  @PreAuthorize("hasPermission(#conference_id, 'page', 'admin-read')")
  fun getWidgets(@PathVariable conference_id: Long?) = ResponseEntity.ok(conferenceRepository.findOne(conference_id).widgets)

  @Transactional
  @PutMapping("/widget")
  @PreAuthorize("hasPermission(#conference_id, 'page', 'update')")
  fun postWidgets(@PathVariable conference_id: Long?, @RequestBody widgets: MutableList<Widget>): ResponseEntity<*> {
    val stored = conferenceRepository.findOne(conference_id)

    stored.widgets = widgets
    conferenceRepository.save(stored)

    return ResponseEntity.ok<List<Widget>>(stored.widgets)
  }

  @GetMapping("/place")
  @PreAuthorize("hasPermission(#conference_id, 'page', 'admin-read')")
  fun getPlaces(@PathVariable conference_id: Long?) = ResponseEntity.ok(conferenceRepository.findOne(conference_id).places)


  @PostMapping("/place")
  @PreAuthorize("hasPermission(#conference_id, 'page', 'update')")
  @ActivityLog(type = ActivityType.CREATE_PLACE)
  fun postPlace(@PathVariable conference_id: Long?, @RequestBody place: Place): ResponseEntity<*> {
    val stored = conferenceRepository.findOne(conference_id)
    place.page = stored
    return ResponseEntity.ok(placeRepository.save(place))
  }
}
