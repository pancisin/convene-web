package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pancisin.bookster.models.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	
	@Query("SELECT notification FROM Notification notification WHERE notification.recipient.id = :user_id AND notification.seen = false")
	public List<Notification> getUserNotifications(@Param("user_id") Long user_id);
}
