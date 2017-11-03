package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pancisin.bookster.models.Message;
import com.pancisin.bookster.models.User;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

	@Query("SELECT message FROM Message message WHERE message.recipientType = 'USER' AND (message.sender.id = :user_id AND message.recipientId = :participant_id) OR (message.recipientId = :user_id AND message.sender.id = :participant_id) ORDER BY message.created DESC")
	public List<Message> getPrivate(@Param("user_id") Long user_id, @Param("participant_id") Long participant_id, Pageable pageable);
	
	@Query("SELECT message FROM Message message WHERE message.recipientId IS NULL AND message.sender.id IN (:users) ORDER BY message.created DESC")
	public List<Message> getPublicUser(@Param("users") List<Long> users, Pageable pageable);
	
	@Query("SELECT message FROM Message message WHERE message.recipientType = 'PAGE' AND message.recipientId = :page_id ORDER BY message.created DESC")
	public List<Message> getPageMessages(@Param("page_id") Long page_id, Pageable pageable);

	@Query("SELECT message FROM Message message WHERE message.recipientType = 'EVENT' AND message.recipientId = :event_id ORDER BY message.created DESC")
	public List<Message> getEventMessages(@Param("event_id") Long event_id, Pageable pageable);
}
