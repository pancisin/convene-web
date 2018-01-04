package com.pancisin.bookster.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import com.pancisin.bookster.model.User;

@Repository
@Transactional
public class UserSearchRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public List<User> search(String text) {
		FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search
				.getFullTextEntityManager(entityManager);

		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(User.class)
				.get();

		Query query = queryBuilder.keyword().onFields("firstName", "lastName", "email")
				.matching(text).createQuery();

		FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, User.class);

		@SuppressWarnings("unchecked")
		List<User> results = jpaQuery.getResultList();

		return results;
	}
}
