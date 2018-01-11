package com.pancisin.bookster.services

import java.net.MalformedURLException
import java.util.Arrays
import java.util.Objects
import java.util.stream.Collectors

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.pancisin.bookster.model.enums.PageState
import com.pancisin.bookster.model.enums.Visibility
import com.pancisin.bookster.repository.ConferenceRepository
import com.pancisin.bookster.repository.EventRepository
import com.pancisin.bookster.repository.PageRepository
import com.redfin.sitemapgenerator.WebSitemapGenerator
import com.redfin.sitemapgenerator.WebSitemapUrl

@Service
class SitemapService {

    @Autowired
    private val eventRepository: EventRepository? = null

    @Autowired
    private val pageRepository: PageRepository? = null

    @Autowired
    private val conferenceRepository: ConferenceRepository? = null

    private val staticRoutes = arrayOf("about", "pricing", "faq", "terms", "privacy-policy", "events", "explore", "conferences")

    @Throws(MalformedURLException::class)
    fun createSitemap(): String? {
        val sitemap = WebSitemapGenerator(BASE_URL)

        val pages = pageRepository!!.findAll().stream()
                .filter { p -> p.state == PageState.PUBLISHED || p.state == PageState.BLOCKED }.map<WebSitemapUrl> { page ->
            try {
                val identifier = if (page.slug == null || page.slug == "")
                    page.id!!.toString()
                else
                    page.slug
                val url = arrayOf(BASE_URL, "page", identifier).joinToString("/")

                return@pageRepository.findAll().stream()
                        .filter(p -> p.getState().equals(PageState.PUBLISHED) || p.getState().equals(PageState.BLOCKED)).map WebSitemapUrl(url)
            } catch (e1: MalformedURLException) {
                e1.printStackTrace()
                return@pageRepository.findAll().stream()
                        .filter(p -> p.getState().equals(PageState.PUBLISHED) || p.getState().equals(PageState.BLOCKED)).map null
            }
        }.filter(Predicate<WebSitemapUrl> { Objects.nonNull(it) }).collect<List<WebSitemapUrl>, Any>(Collectors.toList())

        sitemap.addUrls(pages)

        val events = eventRepository!!.findAll().stream().filter { e -> e.visibility === Visibility.PUBLIC }
                .map<WebSitemapUrl> { event ->
                    try {
                        val url = arrayOf(BASE_URL, "event", event.id!!.toString()).joinToString("/")
                        return@eventRepository.findAll().stream().filter(e -> e.getVisibility() == Visibility.PUBLIC)
                        .map WebSitemapUrl url
                    } catch (e1: MalformedURLException) {
                        return@eventRepository.findAll().stream().filter(e -> e.getVisibility() == Visibility.PUBLIC)
                        .map null
                    }
                }.filter(Predicate<WebSitemapUrl> { Objects.nonNull(it) }).collect<List<WebSitemapUrl>, Any>(Collectors.toList())

        sitemap.addUrls(events)

        val conferences = conferenceRepository!!.findAll().stream()
                .filter { c -> c.state == PageState.PUBLISHED || c.state == PageState.BLOCKED }
                .map<WebSitemapUrl> { conference ->
                    try {
                        val url = arrayOf(BASE_URL, "conference", conference.id!!.toString()).joinToString("/")
                        return@conferenceRepository.findAll().stream()
                                .filter(c -> c.getState().equals(PageState.PUBLISHED) || c.getState().equals(PageState.BLOCKED))
                        .map WebSitemapUrl url
                    } catch (e1: MalformedURLException) {
                        return@conferenceRepository.findAll().stream()
                                .filter(c -> c.getState().equals(PageState.PUBLISHED) || c.getState().equals(PageState.BLOCKED))
                        .map null
                    }
                }.filter(Predicate<WebSitemapUrl> { Objects.nonNull(it) }).collect<List<WebSitemapUrl>, Any>(Collectors.toList())

        sitemap.addUrls(conferences)

        val staticUrls = Arrays.asList(*staticRoutes).stream().map<WebSitemapUrl> { r ->
            try {
                val url = arrayOf(BASE_URL, r).joinToString("/")
                return@Arrays.asList(staticRoutes).stream().map WebSitemapUrl url
            } catch (e1: MalformedURLException) {
                return@Arrays.asList(staticRoutes).stream().map null
            }
        }.filter(Predicate<WebSitemapUrl> { Objects.nonNull(it) }).collect<List<WebSitemapUrl>, Any>(Collectors.toList())

        sitemap.addUrls(staticUrls)

        return sitemap.writeAsStrings().joinToString("")
    }

    companion object {
        private val BASE_URL = "https://convene.sk"
    }
}
