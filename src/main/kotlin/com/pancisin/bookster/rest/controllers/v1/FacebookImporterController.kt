package com.pancisin.bookster.rest.controllers.v1

import java.io.IOException
import java.security.Principal

import javax.transaction.Transactional

import com.pancisin.api.facebookapi.api.FacebookApi
import com.pancisin.api.facebookapi.model.Paginator
import com.pancisin.api.facebookapi.model.Place
import com.pancisin.api.facebookapi.utils.GeoLocation
import com.pancisin.api.facebookapi.utils.Reading
import com.pancisin.bookster.model.Administrator
import com.pancisin.bookster.model.Media
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import com.pancisin.bookster.model.EventBot
import com.pancisin.bookster.model.Page
import com.pancisin.bookster.model.PageImport
import com.pancisin.bookster.model.enums.BotRunState
import com.pancisin.bookster.model.enums.PageRole
import com.pancisin.bookster.repository.EventBotRepository
import com.pancisin.bookster.repository.AdministratorRepository
import com.pancisin.bookster.repository.PageImportRepository
import com.pancisin.bookster.repository.PageRepository
import com.pancisin.bookster.repository.UserRepository
import com.pancisin.bookster.utils.GraphApiPagination

import retrofit2.Call

@RestController
//@PreAuthorize("hasRole('SUPERADMIN')")
@RequestMapping("/api/v1/facebook-importer")
class FacebookImporterController {

  @Autowired
  lateinit var pageRepository: PageRepository

  @Autowired
  lateinit var webSocket: SimpMessagingTemplate

  @Autowired
  lateinit var eventBotRepository: EventBotRepository

  @Autowired
  lateinit var administratorRepository: AdministratorRepository

  @Autowired
  lateinit var userRepository: UserRepository

  @Autowired
  lateinit var pageImportRepository: PageImportRepository

  private val pageFields = "name,about,cover,location,picture.width(640)"
  private val placeFields = arrayOf("name", "categories", "location", "metadata")

  @GetMapping("/search")
  fun searchPages(
    @RequestParam(name = "latitude", required = true) latitude: Double?,
    @RequestParam(name = "longitude", required = true) longitude: Double?,
    @RequestParam(name = "radius", required = false, defaultValue = "1000") radius: Int,
    @RequestParam(name = "limit", required = false, defaultValue = "10") limit: Int,
    @RequestParam(name = "after", required = false, defaultValue = "") after: String,
    @RequestParam(name = "before", required = false, defaultValue = "") before: String,
    @RequestParam(name = "q", required = false, defaultValue = "*") q: String): ResponseEntity<*>? {

    val api = FacebookApi.create()
    var call: Call<Paginator<Place>>? = null
    if (latitude != null && longitude != null) {
      call = api.searchPlaces(
        GeoLocation(latitude, longitude), radius, q,
        Reading().after(after).limit(limit).fields(placeFields.joinToString(",")))
    } else {
      call = api.searchPlaces(q, Reading().limit(limit))
    }

    try {
      val imports = pageImportRepository.findAll()
      call.execute().let { response ->
        if (response.isSuccessful && response.body() != null) {
          val result = response.body()?.data?.map { p ->
            val import = imports.firstOrNull { it.sourceId.equals(p.id) }
            mapOf(
              "name" to p.name,
              "id" to p.id,
              "imported" to (import != null),
              "pageImportId" to import?.id
            )
          }
          return ResponseEntity.ok(GraphApiPagination(result, response.body()?.paging?.cursors?.after ?: ""))
        }
      }
    } catch (ex: IOException) {
    }

    return null
  }

  @GetMapping("/convert")
  fun importPage(@RequestParam(name = "facebook_id") facebookId: String): ResponseEntity<Page> {
    val api = FacebookApi.create()
    try {
      api.getPage(facebookId, Reading().fields(pageFields)).execute().let { response ->
        if (response.isSuccessful && response.body() != null) {
          return ResponseEntity.ok(convertPage(response.body()!!))
        }
      }
    } catch (ex: IOException) {
    }
    return ResponseEntity(HttpStatus.NOT_FOUND)
  }

  @MessageMapping("/page-import")
  fun runEventBot(@Payload requestMap: Map<String, String>, principal: Principal) {
    val facebook_id = requestMap["facebook_id"]
    var import = PageImport(state = BotRunState.RUNNING, sourceId = facebook_id.toString())

    if (facebook_id != null && facebook_id != "" && pageImportRepository.findBySourceId(facebook_id) == null) {
      webSocket.convertAndSendToUser(principal.name, "/queue/page.import", import)

      val api = FacebookApi.create()
      try {
        api.getPage(facebook_id, Reading().fields(pageFields)).execute().let { response ->
          if (response.isSuccessful && response.body() != null) {
            var page: Page? = convertPage(response.body()!!)

            try {
              page = pageRepository.save<Page>(page)

              eventBotRepository.save(EventBot(page = page, fbPageId = facebook_id, active = true))
              val user = userRepository.findByEmail(principal.name)
              administratorRepository.save(Administrator(user = user, page = page, active = true, role = PageRole.ROLE_OWNER))
            } catch (ex: DataIntegrityViolationException) {
              page = pageRepository.findByFacebookId(facebook_id)
              ex.printStackTrace()
            }

            import = pageImportRepository.save(
              import.apply {
                sourceName = page?.name.toString()
                state = BotRunState.SUCCESS
                page = page
              }
            )

            webSocket.convertAndSendToUser(principal.name, "/queue/page.import", import)
          }
        }
      } catch (ex: IOException) { }
    }
  }

  @Transactional
  @MessageMapping("import-replay")
  fun replayImport(@Payload pageImport: PageImport, principal: Principal) {
    val stored = pageImportRepository.findOne(pageImport.id).apply {
      state = BotRunState.RUNNING
    }

    webSocket.convertAndSendToUser(principal.name, "/queue/page.import", stored)

    val api = FacebookApi.Factory.create()
    api.getPage(stored.sourceId.toString(), Reading().fields(pageFields)).execute().let { response ->
      if (response.isSuccessful && response.body() != null) {
        val page = stored.page

        if (page != null) {
          convertPage(response.body()!!).let {
            page.apply {
              name = it.name
              summary = it.summary
              poster = it.poster
            }
          }

          pageRepository.save(page)
        }

        stored.state = BotRunState.SUCCESS
        webSocket.convertAndSendToUser(principal.name, "/queue/page.import", stored)
        return
      }
    }

    stored.state = BotRunState.ERROR
    webSocket.convertAndSendToUser(principal.name, "/queue/page.import", stored)
  }

  private fun convertPage(fbPage: com.pancisin.api.facebookapi.model.Page) = Page().apply {
    name = fbPage.name.toString();
    summary = fbPage.about;
    facebookId = fbPage.id;
    fbPage.picture?.data?.url?.let { poster = Media(it) }
  }
}
