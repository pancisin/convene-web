package com.pancisin.bookster.services

import java.util.ArrayList
import java.util.Calendar

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

import com.pancisin.bookster.model.UserSubscription
import com.pancisin.bookster.model.enums.PageState
import com.pancisin.bookster.model.enums.SubscriptionState
import com.pancisin.bookster.repository.ConferenceRepository
import com.pancisin.bookster.repository.PageRepository
import com.pancisin.bookster.repository.UserSubscriptionRepository

@Component
class LicenseService {

  @Autowired
  lateinit var usRepository: UserSubscriptionRepository

  @Autowired
  lateinit var emailService: EmailService

  @Autowired
  lateinit var pageRepository: PageRepository

  @Autowired
  lateinit var conferenceRepository: ConferenceRepository

  @Scheduled(cron = "0 0 3 * * *")
  fun checkLicenses() {
    val expired = usRepository.findExpirations()
    val newSubs = ArrayList<UserSubscription>()

    expired.forEach { s ->
      if (s.state === SubscriptionState.ACTIVE) {
        s.state = SubscriptionState.EXPIRED
        newSubs.add(createNew(s))
      } else if (s.state === SubscriptionState.NEW) {
        s.state = SubscriptionState.UNPAID

        val user_id = s.user?.id

        var pages = pageRepository.getByOwner(user_id)
        pages.addAll(conferenceRepository.getByOwner(user_id))

        pages = pages
          .filter { it.state === PageState.PUBLISHED || it.state === PageState.DEACTIVATED }
          .onEach { it.state = PageState.BLOCKED }

        pageRepository.save(pages)
      }
    }

    usRepository.save(newSubs)
    newSubs.stream().forEach { emailService.sendInvoiceEmail(it) }

    usRepository.save(expired)
  }

  private fun createNew(previous: UserSubscription) = UserSubscription(
    user = previous.user,
    subscription = previous.subscription,
    state = SubscriptionState.NEW,
    expires = Calendar.getInstance().apply {
      add(Calendar.MONTH, 1)
    }
  )
}
