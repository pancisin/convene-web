package com.pancisin.bookster.repository;

import com.pancisin.bookster.model.PageMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PageAttendeeRepository extends JpaRepository<PageMember, UUID> {

  @Query("SELECT attendee FROM pageAttendee attendee WHERE attendee.page.id = :page_id")
  public List<PageMember> findByPage(@Param("page_id") Long page_id);

  @Query("SELECT attendee FROM pageAttendee attendee WHERE attendee.page.id = :page_id AND attendee.user.id = :user_id")
  public PageMember findByAttendance(@Param("page_id") Long page_id,
                                     @Param("user_id") Long user_id);
}
