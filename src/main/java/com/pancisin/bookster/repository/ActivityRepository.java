package com.pancisin.bookster.repository;

import java.util.List;

import com.pancisin.bookster.repository.custom.ActivityRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long>, ActivityRepositoryCustom {

	@Query("SELECT activity FROM Page page JOIN page.activities activity WHERE page.id = :page_id")
	public Page<Activity> getByPage(@Param("page_id") Long page_id, Pageable pageable);

	@Query("SELECT activity FROM Activity activity WHERE activity.user.id = :user_id")
	public List<Activity> getByUser(@Param("user_id") Long user_id);

//	@Query("SELECT activity FROM Activity activity JOIN activity.page page JOIN page.members page_member JOIN page_member.user user WHERE user.id = :user_id")
//  Page<Activity> getUserActivityFeed(@Param("user_id") Long user_id, Pageable pageable);
}
