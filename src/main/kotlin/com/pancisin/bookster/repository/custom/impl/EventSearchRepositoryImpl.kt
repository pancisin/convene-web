package com.pancisin.bookster.repository.custom.impl

import com.pancisin.bookster.model.Event
import com.pancisin.bookster.repository.custom.EventSearchRepository
import org.hibernate.search.jpa.Search
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@Repository
@Transactional
class EventSearchRepositoryImpl : EventSearchRepository {

  @Autowired
  @PersistenceContext
  lateinit var entityManager: EntityManager

  override fun search(keyword: String): List<Any> {
    val fullTextEntityManager = Search.getFullTextEntityManager(entityManager)
    val queryBuilder = fullTextEntityManager
      .searchFactory
      .buildQueryBuilder()
      .forEntity(Event::class.java)
      .get()

    val query = queryBuilder
      .keyword()
      .onFields("name")
      .matching(keyword)
      .createQuery()

    val jpaQuery = fullTextEntityManager.createFullTextQuery(query, Event::class.java)
    return jpaQuery.resultList.filterNotNull()
  }
}
