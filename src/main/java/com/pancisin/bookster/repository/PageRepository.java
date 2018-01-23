package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.model.Page;

public interface PageRepository extends JpaRepository<Page, Long> {

//	@Override
//	@CacheEvict(value = "pages", key = "#p0.slug")
//	<S extends Page> S save(S entity);

//	@Cacheable("pages")
	public Page findBySlug(String slug);

	@Query("SELECT page FROM Page page JOIN page.branch branch WHERE page.pageType = 'PAGE' AND branch.category.id = :category_id AND (page.state = 'PUBLISHED' OR page.state = 'BLOCKED')")
	public org.springframework.data.domain.Page<Page> findByCategory(@Param("category_id") Long category_id,
			Pageable pageable);

	@Query("SELECT page FROM Page page WHERE page.pageType = 'PAGE' AND page.branch.id = :branch_id AND (page.state = 'PUBLISHED' OR page.state = 'BLOCKED')")
	public org.springframework.data.domain.Page<Page> findByBranch(@Param("branch_id") Long branch_id,
			Pageable pageable);

	@Query("SELECT page FROM Page page WHERE page.pageType = 'PAGE' AND page.state = 'PUBLISHED' OR page.state = 'BLOCKED'")
	public org.springframework.data.domain.Page<Page> findAllVisible(Pageable pageable);

	@Query("SELECT page FROM Page page LEFT JOIN page.members members WHERE page.pageType = 'PAGE' GROUP BY page ORDER BY count(members) DESC, page.name ASC")
	public org.springframework.data.domain.Page<Page> getPopular(Pageable pageable);

	@Query("SELECT page FROM PageMember pageMember JOIN pageMember.user user JOIN pageMember.page page WHERE page.pageType = 'PAGE' AND user.id = :user_id AND (page.state = 'PUBLISHED' OR page.state = 'BLOCKED')")
	public List<Page> getFollowed(@Param("user_id") Long user_id);

	public Page findByFacebookId(@Param("facebookId") String facebookId);

	@Query("SELECT page FROM Page page JOIN page.administrators admin WHERE page.pageType = 'PAGE' AND admin.role = 'ROLE_OWNER' AND admin.user.id = :user_id")
	public List<Page> getByOwner(@Param("user_id") Long user_id);

  @Query("SELECT page FROM Page page JOIN page.administrators admin WHERE page.pageType = 'CONFERENCE' AND admin.role = 'ROLE_OWNER' AND admin.user.id = :user_id")
  public List<Page> getConferencesByOwner(@Param("user_id") Long user_id);

	@Query("SELECT page FROM Page page WHERE page.pageType = 'CONFERENCE' AND (page.state = 'PUBLISHED' OR page.state = 'BLOCKED')")
	public org.springframework.data.domain.Page<Page> getPublicConferences(Pageable pageable);

	@Query("SELECT DISTINCT page FROM Page page LEFT JOIN page.invitations invitation LEFT JOIN page.administrators administrator WHERE page.pageType = 'CONFERENCE' AND ((invitation.user.id = :user_id AND (page.state = 'PUBLISHED' OR page.state = 'BLOCKED')) OR administrator.user.id = :user_id)")
	public org.springframework.data.domain.Page<Page> getConferencesForUser(@Param("user_id") Long user_id, Pageable pageable);
}
