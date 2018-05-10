package com.pancisin.bookster.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import javax.persistence.EntityManager
import org.hibernate.search.jpa.Search


@Component
class LuceneIndexBuilderService : ApplicationListener<ApplicationReadyEvent> {

  @Autowired
  lateinit var entityManager: EntityManager

  override fun onApplicationEvent(event: ApplicationReadyEvent?) {
    try {
      val fullTextEntityManager = Search.getFullTextEntityManager(entityManager)
      fullTextEntityManager.createIndexer().startAndWait()
    } catch (e: InterruptedException) {
      println("An error occurred trying to build the serach index: " + e.toString())
    }

    return
  }
}
