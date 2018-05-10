package com.pancisin.bookster.repository.custom.impl

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

import org.springframework.stereotype.Repository

import com.pancisin.bookster.model.User
import com.pancisin.bookster.repository.custom.ISearchRepository
import com.pancisin.bookster.repository.custom.IUserSearchRepository
import org.hibernate.search.jpa.Search
import org.springframework.beans.factory.annotation.Autowired

@Repository
@Transactional
class UserSearchRepository : IUserSearchRepository {

  @Autowired
  @PersistenceContext
  lateinit var entityManager: EntityManager

  override fun search(keyword: String): List<Any> {
    val fullTextEntityManager = Search.getFullTextEntityManager(entityManager)
    val queryBuilder = fullTextEntityManager
      .searchFactory
      .buildQueryBuilder()
      .forEntity(User::class.java)
      .get()

    val query = queryBuilder
      .keyword()
      .onFields("firstName", "lastName", "email")
      .matching(keyword)
      .createQuery()

    val jpaQuery = fullTextEntityManager.createFullTextQuery(query, User::class.java)
    return jpaQuery.resultList.filterNotNull()
  }
}
