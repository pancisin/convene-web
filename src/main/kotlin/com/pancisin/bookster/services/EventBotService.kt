package com.pancisin.bookster.services

import java.math.BigDecimal
import java.util.*

// import com.pancisin.api.facebookapi.api.FacebookApi
// import com.pancisin.api.facebookapi.model.Event
// import com.pancisin.api.facebookapi.utils.Reading
import com.pancisin.bookster.model.Activity
import com.pancisin.bookster.model.Media
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

import com.pancisin.bookster.model.EventBot
import com.pancisin.bookster.model.BotRun
import com.pancisin.bookster.model.enums.*
import com.pancisin.bookster.repository.ActivityRepository
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

  @Autowired
  lateinit var activityFeedService: ActivityFeedService

//  val eventFields = "name,description,place,id,start_time,picture.type(large)"
  val eventFields = "name,description,place,id,start_time,cover"

  @Scheduled(cron = "0 0 6 * * *")
  fun run(): List<BotRun>? {
    // val bots = eventBotRepository.findAll()
    // val runs: MutableList<BotRun> = ArrayList()

    // val api = FacebookApi.create()
    // bots.forEach { if (it.active) runs.add(run(it, api)) }

    // return botRunRepository.save(runs)

    throw UnsupportedOperationException("Not implemented!")
  }

  fun run(bot: EventBot): BotRun {
    // val api = FacebookApi.create()
    // return botRunRepository.save(this.run(bot, api))
    
    throw UnsupportedOperationException("Not implemented!")
}

  // private fun run(
  //   bot: EventBot, 
  //   api: FacebookApi
  //   ): BotRun {
  //   var savedEventsCount = 0
  //   val facebookId = bot.fbPageId

  //   facebookId?.let {
  //     api.getEvents(facebookId, Reading().fields(eventFields).since(Date())).execute().let { response ->
  //       if (response.isSuccessful && response.body() != null) {
  //         response.body()?.data?.map(this::buildEvent)?.forEach {
  //           try {
  //             it.page = bot.page
  //             eventRepository.save(it)

  //             activityFeedService.publishActivity(Activity(
  //               page = it.page,
  //               type = ActivityType.CREATE_EVENT,
  //               objectType = ObjectType.EVENT,
  //               objectId = it.id.toString()
  //             ))

  //             savedEventsCount++
  //           } catch (ex: Exception) {
  //           }
  //         }
  //       }
  //     }
  //   }

  //   return BotRun(bot, BotRunState.SUCCESS).apply { dataCount = savedEventsCount }
  // }

  private fun buildEvent(
    // ev: Event
    ) = com.pancisin.bookster.model.Event().apply {
    // name = ev.name;
    // summary = ev.description;
    // facebookId = ev.id;
    // visibility = Visibility.PUBLIC;
    // state = PageState.PUBLISHED
    // date = ev.startTime

    // ev.cover?.let {
    //   poster = Media(it.source ?: "")
    // }

    // ev.place?.location?.let {
    //   latitude = BigDecimal.valueOf(it.latitude ?: 0.0)
    //   longitude = BigDecimal.valueOf(it.longitude ?: 0.0)
    // }

    throw UnsupportedOperationException("Not implemented!")
  }
}

