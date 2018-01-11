package com.pancisin.bookster.services

import java.io.Writer

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import com.pancisin.bookster.services.SitemapService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.view.AbstractView

@Component
class SitemapView : AbstractView() {

  @Autowired
  lateinit var service: SitemapService

  @Throws(Exception::class)
  override fun renderMergedOutputModel(
    model: Map<String, Any>, request: HttpServletRequest,
    response: HttpServletResponse) {

    response.contentType = MediaType.APPLICATION_XML_VALUE
    response.writer.use { writer -> writer.append(service.createSitemap()) }
  }
}
