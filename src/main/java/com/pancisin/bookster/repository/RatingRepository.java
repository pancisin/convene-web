package com.pancisin.bookster.repository;

import com.pancisin.bookster.model.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, UUID> {

  @Query("SELECT rating FROM Rating rating WHERE rating.event.id = :event_id")
  public Page<Rating> getForEvent(@Param("event_id") Long event_id, Pageable pageable);
}
