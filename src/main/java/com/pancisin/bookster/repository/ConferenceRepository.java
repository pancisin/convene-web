package com.pancisin.bookster.repository;

import com.pancisin.bookster.model.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConferenceRepository extends JpaRepository<Page, Long> {

  @Query("SELECT page FROM Page page WHERE page.slug = :slug AND page.pageType = 'CONFERENCE'")
  Page findBySlug(@Param("slug") String slug);

  @Override
  @Query("SELECT page FROM Page page WHERE page.id = :conference_id AND page.pageType = 'CONFERENCE'")
  Page findOne(@Param("conference_id") Long conference_id);

  @Query("SELECT page FROM Page page JOIN page.events event WHERE page.pageType = 'CONFERENCE' AND DATE(event.date) >= CURDATE() AND (page.state = 'PUBLISHED' OR page.state = 'BLOCKED') AND page.visibility = 'PUBLIC'")
  public org.springframework.data.domain.Page<Page> findPublic(Pageable pageable);

  @Query("SELECT page FROM Page page JOIN page.administrators admin WHERE page.pageType = 'CONFERENCE' AND admin.role = 'ROLE_OWNER' AND admin.user.id = :user_id AND page.state != 'DELETED'")
  public List<Page> getByOwner(@Param("user_id") Long user_id);

  @Query("SELECT DISTINCT page FROM Page page LEFT JOIN page.invitations invitation LEFT JOIN page.administrators administrator JOIN page.events event WHERE page.pageType = 'CONFERENCE' AND (((page.visibility = 'PUBLIC' OR page.visibility = 'AUTHENTICATED') OR (page.visibility = 'INVITED' AND invitation.user.id = :user_id)) AND (page.state = 'PUBLISHED' OR page.state = 'BLOCKED') AND DATE(event.date) >= CURDATE() OR (administrator.user.id = :user_id AND page.state != 'DELETED'))")
  public org.springframework.data.domain.Page<Page> getForUser(@Param("user_id") Long user_id, Pageable pageable);
}
