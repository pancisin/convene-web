package com.pancisin.bookster.services

import java.net.MalformedURLException

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.pancisin.bookster.model.enums.PageState
import com.pancisin.bookster.model.enums.Visibility
import com.pancisin.bookster.repository.ConferenceRepository
import com.pancisin.bookster.repository.EventRepository
import com.pancisin.bookster.repository.PageRepository
import com.redfin.sitemapgenerator.WebSitemapGenerator
import com.redfin.sitemapgenerator.WebSitemapUrl
import java.util.*

@Service
class SitemapService {
  private final val BASE_URL: String = "https://convene.sk"

  @Autowired
  lateinit var eventRepository: EventRepository

  @Autowired
  lateinit var pageRepository: PageRepository

  @Autowired
  lateinit var conferenceRepository: ConferenceRepository

  private val staticRoutes = arrayOf("about", "pricing", "faq", "terms", "privacy-policy", "events", "explore", "conferences")

  fun createSitemap(): String? {
    val sitemap = WebSitemapGenerator(BASE_URL)

    val pages = pageRepository.findAll().filter { it.state === PageState.PUBLISHED || it.state === PageState.BLOCKED }.mapNotNull { page ->
      try {
        val identifier = if (page.slug?.equals("") ?: true) page.id.toString() else page.slug
        WebSitemapUrl("/${BASE_URL}/page/${identifier}")
      } catch (ex: MalformedURLException) {
        null
      }
    }
    sitemap.addUrls(pages)

    val events = eventRepository.findAll().filter {
      it.visibility === Visibility.PUBLIC && it.state === PageState.PUBLISHED && it.date?.compareTo(Calendar.getInstance()) ?: -1 >= 0
    }.mapNotNull {
      try {
        WebSitemapUrl("/${BASE_URL}/event/${it?.id.toString()}")
      } catch (ex: MalformedURLException) {
        null
      }
    }
    sitemap.addUrls(events)

    val conferences = conferenceRepository.findAll().filter { it.state === PageState.PUBLISHED || it.state === PageState.BLOCKED }.map {
      try {
        WebSitemapUrl("/${BASE_URL}/conference/${it.id}")
      } catch (ex: MalformedURLException) {
        null
      }
    }
    sitemap.addUrls(conferences)

    val staticUrls = staticRoutes.map {
      try {
        WebSitemapUrl("/${BASE_URL}/${it}")
      } catch (ex: MalformedURLException) {
        null
      }
    }
    sitemap.addUrls(staticUrls)

    return sitemap.writeAsStrings().joinToString("")
  }
}
