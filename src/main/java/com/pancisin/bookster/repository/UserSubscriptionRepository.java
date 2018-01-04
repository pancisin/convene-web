package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pancisin.bookster.model.UserSubscription;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

	@Query("SELECT us FROM UserSubscription us WHERE (us.state = 'ACTIVE' OR us.state = 'NEW') AND us.expires < NOW()")
	public List<UserSubscription> findExpirations();

	public UserSubscription findById(String id);
}
