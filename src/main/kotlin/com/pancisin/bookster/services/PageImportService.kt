package com.pancisin.bookster.services

// import com.pancisin.api.facebookapi.api.FacebookApi
// import com.pancisin.api.facebookapi.utils.Reading
// import com.pancisin.bookster.exceptions.FacebookPageNotFoundException
import com.pancisin.bookster.exceptions.PageImportFailedException
import com.pancisin.bookster.model.Media
import com.pancisin.bookster.model.Page
import com.pancisin.bookster.model.PageImport
import com.pancisin.bookster.model.enums.BotRunState
import com.pancisin.bookster.repository.PageImportRepository
import com.pancisin.bookster.repository.PageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PageImportService {
  private val pageFields = "name,about,cover,location,picture.width(640)"

  @Autowired
  lateinit var pageRepository: PageRepository

  @Autowired
  lateinit var pageImportRepository: PageImportRepository

  @Scheduled(cron = "0 0 5 * * *")
  fun pageImportSchedulerRun() = pageImportRepository.findAll().forEach { this.reimportPage(it) }

  fun fetchAndConvert(facebookId: String): Page {
    // val api = FacebookApi.create()
    // api.getPage(facebookId, Reading().fields(pageFields)).execute().let { response ->
    //   val body = response.body()
    //   if (response.isSuccessful && body != null) {
    //     return convertPage(body)
    //   } else throw FacebookPageNotFoundException("Facebook page not found.")
    // }

  throw UnsupportedOperationException("Not implemented!")
  }

  fun reimportPage(pageImport: PageImport): PageImport {
    // val api = FacebookApi.Factory.create()
    // api.getPage(pageImport.sourceId, Reading().fields(pageFields)).execute().let { response ->
    //   val body = response.body()
    //   if (response.isSuccessful && body != null) {
    //     val page = pageImport.page

    //     if (page != null) {
    //       convertPage(body).let {
    //         page.apply {
    //           name = it.name
    //           summary = it.summary
    //           poster = it.poster
    //         }
    //       }

    //       pageImport.page = pageRepository.save(page)
    //       return pageImport
    //     }
    //   }
    // }

    throw PageImportFailedException("Response body is invalid somehow.")
  }

  companion object {
    // fun convertPage(fbPage: com.pancisin.api.facebookapi.model.Page) = Page().apply {
    //   name = fbPage.name.toString();
    //   summary = fbPage.about;
    //   facebookId = fbPage.id;
    //   fbPage.picture?.data?.url?.let { poster = Media(it) }
    // }
  }
}
