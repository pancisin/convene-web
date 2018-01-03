package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.models.Page;

public interface PageRepository extends JpaRepository<Page, Long> {

//	@Override
//	@CacheEvict(value = "pages", key = "#p0.slug")
//	<S extends Page> S save(S entity);

//	@Cacheable("pages")
	public Page findBySlug(String slug);
	
	@Query("SELECT page FROM Page page JOIN page.branch branch JOIN branch.category category WHERE category.id = :category_id AND (page.state = 'PUBLISHED' OR page.state = 'BLOCKED')")
	public org.springframework.data.domain.Page<Page> findByCategory(@Param("category_id") Long category_id,
			Pageable pageable);

	@Query("SELECT page FROM Page page JOIN page.branch branch WHERE branch.id = :branch_id AND (page.state = 'PUBLISHED' OR page.state = 'BLOCKED')")
	public org.springframework.data.domain.Page<Page> findByBranch(@Param("branch_id") Long branch_id,
			Pageable pageable);

	@Query("SELECT page FROM Page page WHERE page.state = 'PUBLISHED' OR page.state = 'BLOCKED'")
	public org.springframework.data.domain.Page<Page> findAllVisible(Pageable pageable);

	@Query("SELECT page FROM Page page LEFT JOIN page.followers followers GROUP BY page ORDER BY count(followers) DESC, page.name ASC")
	public org.springframework.data.domain.Page<Page> getPopular(Pageable pageable);

	@Query("SELECT page FROM Page page JOIN page.followers user WHERE user.id = :user_id AND (page.state = 'PUBLISHED' OR page.state = 'BLOCKED')")
	public List<Page> getFollowed(@Param("user_id") Long user_id);

	public Page findByFacebookId(@Param("facebookId") String facebookId);
	
	@Query("SELECT page FROM Page page JOIN page.administrators admin WHERE admin.role = 'ROLE_OWNER' AND admin.user.id = :user_id")
	public List<Page> getByOwner(@Param("user_id") Long user_id);
}
