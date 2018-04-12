package com.pancisin.bookster.rest.controllers.v1

import com.pancisin.bookster.components.annotations.LicenseLimit
import com.pancisin.bookster.components.storage.StorageService
import com.pancisin.bookster.model.*
import com.pancisin.bookster.model.enums.*
import com.pancisin.bookster.model.enums.Locale
import com.pancisin.bookster.repository.*
import com.pancisin.bookster.rest.controllers.exceptions.InvalidRequestException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.stream.Collectors
import javax.transaction.Transactional
import javax.validation.Valid

@RestController
@RequestMapping("api/v1/user/me")
class MeController {

  @Autowired
  lateinit var userRepository: UserRepository

  @Autowired
  lateinit var notificationRepository: NotificationRepository

  @Autowired
  lateinit var pageRepository: PageRepository

  @Autowired
  lateinit var administratorRepository: AdministratorRepository

  @Autowired
  lateinit var usRepository: UserSubscriptionRepository

  @Autowired
  lateinit var mediaRepository: MediaRepository

  @Autowired
  lateinit var storageService: StorageService

  @Autowired
  lateinit var conferenceRepository: ConferenceRepository

  @Autowired
  lateinit var activityRepository: ActivityRepository

  @Autowired
  lateinit var eventRepository: EventRepository

  @GetMapping
  fun getMe(): ResponseEntity<User> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    return ResponseEntity.ok(userRepository.findOne(auth.id))
  }

  @PutMapping
  @Transactional
  fun update(@RequestBody user: User): ResponseEntity<User> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    val stored = userRepository.findOne(auth.id).apply {
      firstName = user.firstName
      lastName = user.lastName
      address = user.address
      metadata = user.metadata
    }

    if (user.profilePictureData != null && storageService.isBinary(user.profilePictureData)) {
      var profilePicture = Media()
      // profilePicture.setAuthor(stored);
      profilePicture = mediaRepository.save(profilePicture)
      val url = "images/users/" + profilePicture.id!!.toString()

      profilePicture.path = "/files/$url.jpg"
      val size = storageService.storeBinary(user.profilePictureData, url)
      profilePicture.size = size
      stored.profilePicture = profilePicture
    }

    return ResponseEntity.ok(userRepository.save(stored))
  }

  @GetMapping("/notification/{page}/{size}")
  fun getNotifications(
    @PathVariable page: Int,
    @PathVariable size: Int,
    @RequestParam(name = "seen", defaultValue = "false") seen: Boolean): ResponseEntity<*> {

    val auth = SecurityContextHolder.getContext().authentication.principal as User
    val notifications = notificationRepository.findByRecipientId(auth.id, seen, PageRequest(page, size, Sort(Sort.Direction.DESC, "created")))
    return ResponseEntity.ok<Page<Notification>>(notifications)
  }

  @GetMapping("/event/{page}/{size}")
  fun getEvents(
    @PathVariable page: Int,
    @PathVariable size: Int,
    @RequestParam("fromDate") fromDate: String,
    @RequestParam("toDate") toDate: String): ResponseEntity<*> {

    val auth = SecurityContextHolder.getContext().authentication.principal as User
    return ResponseEntity.ok<Page<Event>>(eventRepository.getOwned(auth.id, PageRequest(page, size), fromDate, toDate))
  }

  @PostMapping("/event")
  @LicenseLimit(entity = "event")
  fun postEvent(@Valid @RequestBody event: Event, bindingResult: BindingResult): ResponseEntity<*> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    val stored = userRepository.findOne(auth.id)

    if (bindingResult.hasErrors())
      throw InvalidRequestException("Invalid data", bindingResult)

    event.owner = stored
    return ResponseEntity.ok(eventRepository.save(event))
  }

  @GetMapping("/event/attending")
  fun getAttendingEvents(): ResponseEntity<*> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    return ResponseEntity.ok(eventRepository.getAttending(auth.id))
  }

  @GetMapping("/page")
  fun getPage(): ResponseEntity<*> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    return ResponseEntity.ok(pageRepository.getByOwner(auth.id))
  }

  @PostMapping("/page")
  @LicenseLimit(entity = "page")
  fun postPage(@RequestBody page: com.pancisin.bookster.model.Page): ResponseEntity<*> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User

    val stored_page = pageRepository.save(page)
    val pa = Administrator(
      page = page,
      user = auth,
      active = true,
      role = PageRole.ROLE_OWNER
    )

    administratorRepository.save(pa)

    return ResponseEntity.ok(stored_page)
  }

  @GetMapping("/conference")
  fun getConference(): ResponseEntity<*> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    return ResponseEntity.ok(conferenceRepository.getByOwner(auth.id))
  }

  @PostMapping("/conference")
  @LicenseLimit(entity = "conference")
  @Transactional
  fun postConference(@RequestBody conference: com.pancisin.bookster.model.Page): ResponseEntity<*> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User

    conference.pageType = PageType.CONFERENCE

    val stored_conference = pageRepository.save(conference)

    val ca = Administrator(page = stored_conference, user = auth, active = true, role = PageRole.ROLE_OWNER)
    administratorRepository.save(ca)

    return ResponseEntity.ok(stored_conference)
  }

  @PutMapping("/locale")
  fun changeLocale(@RequestBody locale: Locale): ResponseEntity<*> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    val stored = userRepository.findOne(auth.id).apply {
      this.locale = locale
    }

    return ResponseEntity.ok(userRepository.save(stored))
  }

  @GetMapping("/subscription")
  fun getSubscriptions(): ResponseEntity<*> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    val stored = userRepository.findOne(auth.id)
    return ResponseEntity.ok(stored.subscriptions)
  }

  @Transactional
  @PostMapping("/subscription")
  @Throws(Exception::class)
  fun postSubscription(@RequestBody su: UserSubscription): ResponseEntity<*> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    val stored = userRepository.findOne(auth.id)

    if (stored.license.subscription === Subscription.FREE) {
      if (!stored.verified)
        throw Exception("User email is not verified !")

      if (stored.role !== Role.ROLE_AUTHOR) {
        stored.apply {
          role = Role.ROLE_AUTHOR
        }
      }

      stored.address = su.user!!.address
      userRepository.save(stored)

      su.apply {
        state = SubscriptionState.ACTIVE
        user = stored

        val expiration = Calendar.getInstance()
        expiration.add(Calendar.MONTH, 1)
        expires = expiration
      }

      return ResponseEntity.ok(usRepository.save(su))
    } else
      throw Exception("User already have another license subscribed.")
  }

  @GetMapping("/contacts")
  fun getContacts(): ResponseEntity<*> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    return ResponseEntity.ok(administratorRepository.getContacts(auth.id))
  }

  @GetMapping("/followed-pages")
  fun getFollowedPages(): ResponseEntity<*> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    return ResponseEntity.ok(pageRepository.getFollowed(auth.id))
  }

  @GetMapping("/media")
  fun getMedias(): ResponseEntity<*> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    return ResponseEntity.ok(mediaRepository.getByAuthor(auth.id))
  }

  @Transactional
  @PatchMapping("/set-notifications-seen")
  fun setNotificationsSeen(
    @RequestParam("since") sinceTimestamp: Long?,
    @RequestParam("until") untilTimestamp: Long?,
    @RequestParam("seen") seen: Boolean): ResponseEntity<*> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User

    val sinceCal = Calendar.getInstance()
    sinceCal.timeInMillis = sinceTimestamp!!

    val untilCal = Calendar.getInstance()
    untilCal.timeInMillis = untilTimestamp!!

    return ResponseEntity.ok(notificationRepository.setSeen(auth.id, sinceCal, untilCal, seen))
  }

  @PatchMapping("/privacy-constraints")
  fun patchPrivacyConstraints(@RequestBody constraints: Map<String, PrivacyAccess>): ResponseEntity<*> {
    val constr = constraints.filter { it.value !== PrivacyAccess.PUBLIC }
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    val stored = userRepository.findOne(auth.id).apply {
      privacyConstraints = constr.toMutableMap()
    }

    userRepository.save(stored)
    return ResponseEntity.ok(constraints)
  }

  @GetMapping("/privacy-constraints")
  fun getPrivacyConstraints(): ResponseEntity<*> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    val stored = userRepository.findOne(auth.id)
    return ResponseEntity.ok<Map<String, PrivacyAccess>>(stored.privacyConstraints)
  }

  @GetMapping("/activity-feed/{page}/{size}")
  fun getActivityFeed(@PathVariable page: Int, @PathVariable size: Int): ResponseEntity<*> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    return ResponseEntity.ok<Page<Activity>>(activityRepository.getUserActivityFeed(auth.id!!, PageRequest(page, size)))
  }

  @GetMapping("/suggested-pages/{page}/{size}")
  fun getSuggestedPages(
    @PathVariable page: Int,
    @PathVariable size: Int,
    @RequestParam(name = "except", required = false) except: MutableList<Long>?): ResponseEntity<*> {

    var except = except
    val auth = SecurityContextHolder.getContext().authentication.principal as User

    if (except == null) {
      except = ArrayList()
      except.add(0L)
    }

    return ResponseEntity.ok<Page<com.pancisin.bookster.model.Page>>(pageRepository.getSuggested(auth.id, except, PageRequest(page, size)))
  }
}
