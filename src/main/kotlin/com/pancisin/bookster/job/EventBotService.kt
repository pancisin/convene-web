package com.pancisin.bookster.job

import java.math.BigDecimal
import java.util.*

import com.pancisin.api.facebookapi.api.FacebookApi
import com.pancisin.api.facebookapi.model.Event
import com.pancisin.api.facebookapi.utils.Reading
import com.pancisin.bookster.model.Media
import org.hibernate.exception.ConstraintViolationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

import com.pancisin.bookster.model.EventBot
import com.pancisin.bookster.model.EventBotRun
import com.pancisin.bookster.model.enums.BotRunState
import com.pancisin.bookster.model.enums.PageState
import com.pancisin.bookster.model.enums.Visibility
import com.pancisin.bookster.repository.EventBotRepository
import com.pancisin.bookster.repository.EventBotRunRepository
import com.pancisin.bookster.repository.EventRepository
import com.pancisin.bookster.repository.PlaceRepository

import kotlin.collections.ArrayList

@Component
class EventBotService {

  @Autowired
  lateinit var eventBotRepository: EventBotRepository

  @Autowired
  lateinit var eventRepository: EventRepository

  @Autowired
  lateinit var eventBotRunRepository: EventBotRunRepository

  val eventFields = "name,description,place,id,start_time,picture.type(large)"

  @Scheduled(cron = "0 0 6 * * *")
  fun run(): List<EventBotRun>? {
    val bots = eventBotRepository.findAll()
    val runs: MutableList<EventBotRun> = ArrayList()

    val api = FacebookApi.create()
    bots.forEach { if (it.active) runs.add(run(it, api)) }

    return eventBotRunRepository.save(runs)
  }

  fun run(bot: EventBot): EventBotRun {
    val api = FacebookApi.create()
    return eventBotRunRepository.save(this.run(bot, api))
  }

  private fun run(bot: EventBot, api: FacebookApi): EventBotRun {
    var savedEventsCount = 0
    val facebookId = bot.fbPageId

    facebookId?.let {
      api.getEvents(facebookId, Reading().fields(eventFields).since(Date())).execute().let { response ->
        if (response.isSuccessful && response.body() != null) {
          val events = response.body()?.data?.map(this::buildEvent) ?: ArrayList()

          for (e in events) {
            try {
              eventRepository.save(e)
              savedEventsCount++
            } catch (ex: Exception) { }
          }
        }
      }
    }

    return EventBotRun(bot = bot, state = BotRunState.SUCCESS, eventsCount = savedEventsCount)
  }

  private fun buildEvent(ev: Event) = com.pancisin.bookster.model.Event().apply {
    name = ev.name;
    summary = ev.description;
    facebookId = ev.id;
    visibility = Visibility.PUBLIC;
    state = PageState.PUBLISHED

    if (ev.startTime != null) {
      date = Calendar.getInstance().apply {
        time = ev.startTime;
        timeZone = TimeZone.getTimeZone("UTC")
      }
    }

    ev.picture?.data?.let {
      poster = Media(it.url ?: "")
    }

    ev.place?.location?.let {
      latitude = BigDecimal.valueOf(it.latitude ?: 0.0)
      longitude = BigDecimal.valueOf(it.longitude ?: 0.0)
    }
  }
}

