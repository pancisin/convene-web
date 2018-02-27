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
  @Query("SELECT page FROM Page page WHERE page.slug = :slug AND page.pageType = 'PAGE'")
	Page findBySlug(@Param("slug") String slug);

  @Override
  @Query("SELECT page FROM Page page WHERE page.id = :page_id AND page.pageType = 'PAGE'")
  Page findOne(@Param("page_id") Long page_id);

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

	@Query("SELECT page FROM PageMember pageMember JOIN pageMember.user user JOIN pageMember.page page WHERE pageMember.active = 1 AND page.pageType = 'PAGE' AND user.id = :user_id AND (page.state = 'PUBLISHED' OR page.state = 'BLOCKED')")
	public List<Page> getFollowed(@Param("user_id") Long user_id);

	public Page findByFacebookId(@Param("facebookId") String facebookId);

	@Query("SELECT page FROM Page page JOIN page.administrators admin WHERE page.pageType = 'PAGE' AND admin.role = 'ROLE_OWNER' AND admin.user.id = :user_id AND page.state != 'DELETED'")
	public List<Page> getByOwner(@Param("user_id") Long user_id);

//  AND page.id NOT IN (:except_pages)

	@Query("SELECT page FROM Page page LEFT JOIN page.members pageMember WHERE (pageMember IS NULL OR (pageMember.user.id = :user_id AND pageMember.active = 0)) AND page.id NOT IN (:except_pages)")
  public org.springframework.data.domain.Page<Page> getSuggested(@Param("user_id") Long user_id,
                                                                 @Param("except_pages") List<Long> except_pages,
                                                                 Pageable pageable);
}
