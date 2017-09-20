package com.pancisin.bookster.repository;

import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pancisin.bookster.models.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

	@Override
	@Cacheable("notifications")
	Notification findOne(Long id);

	@Override
	@CacheEvict(value = "notifications", key = "#p0.id")
	<S extends Notification> S save(S entity);

	@Cacheable(value = "notifications")
	public Page<Notification> findByRecipientId(Long recipientId, Pageable pageable);
	
	public Notification findById(UUID id);
}
