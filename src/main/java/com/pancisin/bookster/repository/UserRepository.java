package com.pancisin.bookster.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pancisin.bookster.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//	@Override
//	@Cacheable("users")
//	User findOne(Long id);
//
//	@Override
//	@CacheEvict(value = "users", key = "#p0.user.id")
//	<S extends User> S save(S entity);
//	
//	@Cacheable("users")
	User findByEmail(String email);
	User findByFacebookId(Long facebookId);
}
