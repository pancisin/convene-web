package com.pancisin.bookster.services

import java.math.BigDecimal
import java.util.*

import com.pancisin.api.facebookapi.api.FacebookApi
import com.pancisin.api.facebookapi.model.Event
import com.pancisin.api.facebookapi.utils.Reading
import com.pancisin.bookster.model.Media
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

import com.pancisin.bookster.model.EventBot
import com.pancisin.bookster.model.BotRun
import com.pancisin.bookster.model.enums.BotRunState
import com.pancisin.bookster.model.enums.PageState
import com.pancisin.bookster.model.enums.Visibility
import com.pancisin.bookster.repository.EventBotRepository
import com.pancisin.bookster.repository.BotRunRepository
import com.pancisin.bookster.repository.EventRepository

import kotlin.collections.ArrayList

@Component
class EventBotService {

  @Autowired
  lateinit var eventBotRepository: EventBotRepository

  @Autowired
  lateinit var eventRepository: EventRepository

  @Autowired
  lateinit var botRunRepository: BotRunRepository

  val eventFields = "name,description,place,id,start_time,picture.type(large)"

  @Scheduled(cron = "0 0 6 * * *")
  fun run(): List<BotRun>? {
    val bots = eventBotRepository.findAll()
    val runs: MutableList<BotRun> = ArrayList()

    val api = FacebookApi.create()
    bots.forEach { if (it.active) runs.add(run(it, api)) }

    return botRunRepository.save(runs)
  }

  fun run(bot: EventBot): BotRun {
    val api = FacebookApi.create()
    return botRunRepository.save(this.run(bot, api))
  }

  private fun run(bot: EventBot, api: FacebookApi): BotRun {
    var savedEventsCount = 0
    val facebookId = bot.fbPageId

    facebookId?.let {
      api.getEvents(facebookId, Reading().fields(eventFields).since(Date())).execute().let { response ->
        if (response.isSuccessful && response.body() != null) {
          response.body()?.data?.map(this::buildEvent)?.forEach {
            try {
              it.page = bot.page
              eventRepository.save(it)
              savedEventsCount++
            } catch (ex: Exception) {
            }
          }
        }
      }
    }

    return BotRun(bot, BotRunState.SUCCESS).apply { dataCount = savedEventsCount }
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

