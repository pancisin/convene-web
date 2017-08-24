package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.models.PageAdministrator;
import com.pancisin.bookster.models.User;

public interface PageAdministratorRepository extends JpaRepository<PageAdministrator, Long> {

	// @Query("SELECT user FROM PageAdministrator pa JOIN pa.user user JOIN
	// pa.page page WHERE pa.role='ROLE_OWNER' AND page.id = :page.id")
	// public User getPageOwner(Page page);

	@Query("SELECT DISTINCT user FROM PageAdministrator pa JOIN pa.user user WHERE pa.page.id IN "
			+ "(SELECT ipa.page.id FROM PageAdministrator ipa WHERE ipa.user.id = :user_id) AND user.id != :user_id")
	public List<User> getContacts(@Param("user_id") Long user_id);
}
