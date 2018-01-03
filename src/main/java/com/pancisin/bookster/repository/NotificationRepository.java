package com.pancisin.bookster.repository;

import java.util.Calendar;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pancisin.bookster.models.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

	// @Override
	// @Cacheable("notifications")
	// Notification findOne(UUID id);
	//
	// @Override
	// @CacheEvict(value = "notifications", key = "#p0.id")
	// <S extends Notification> S save(S entity);
	//
	// @Cacheable(value = "notifications")
	@Query("SELECT n FROM Notification n WHERE n.recipient.id = :recipientId AND n.seen = :seen")
	public Page<Notification> findByRecipientId(@Param("recipientId") Long recipientId, @Param("seen") boolean seen, Pageable pageable);

	@Modifying
	@Query("UPDATE Notification n SET n.seen = ABS(n.seen - 1) WHERE n.id = :notificationId")
	public int toggleSeen(@Param("notificationId") UUID notificationId);

	@Modifying
	@Query("UPDATE Notification n SET n.seen = :seen WHERE n.recipient.id = :userId AND (n.created BETWEEN :since AND :until)")
	public int setSeen(@Param("userId") Long userId, @Param("since") Calendar since, @Param("until") Calendar until,
			@Param("seen") boolean seen);
}
