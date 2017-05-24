package com.pancisin.bookster.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import com.pancisin.bookster.models.User;

@Repository
@Transactional
public class UserSearchRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public List search(String text) {
		FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search
				.getFullTextEntityManager(entityManager);

		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(User.class)
				.get();

		org.apache.lucene.search.Query query = queryBuilder.keyword().onFields("firstName", "lastName", "email")
				.matching(text).createQuery();

		org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, User.class);

		@SuppressWarnings("unchecked")
		List results = jpaQuery.getResultList();

		return results;
	}
}
