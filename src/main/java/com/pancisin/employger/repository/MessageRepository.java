package com.pancisin.employger.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pancisin.employger.models.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

	@Query("SELECT message FROM Message message WHERE (message.sender.id = :user_id AND message.recipient.id = :participant_id) OR (message.recipient.id = :user_id AND message.sender.id = :participant_id)")
	public List<Message> getPrivate(@Param("user_id") Long user_id, @Param("participant_id") Long participant_id);
	
}
