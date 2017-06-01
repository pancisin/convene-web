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

	@Query("SELECT DISTINCT user FROM PageAdministrator pa JOIN pa.user user JOIN pa.page page WHERE page.id IN "
			+ "(SELECT ipage.id FROM PageAdministrator ipa JOIN ipa.user iuser JOIN ipa.page ipage WHERE iuser.id = :user_id AND "
			+ " ipa.role = 'ROLE_OWNER') AND pa.role != 'ROLE_OWNER'")
	public List<User> getContacts(@Param("user_id") Long user_id);
}
