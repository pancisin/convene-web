package com.pancisin.bookster.repository;

import java.util.List;

import com.pancisin.bookster.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.model.User;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {

	// @Query("SELECT user FROM PageAdministrator pa JOIN pa.user user JOIN
	// pa.page page WHERE pa.role='ROLE_OWNER' AND page.id = :page.id")
	// public User getPageOwner(Page page);

	@Query("SELECT DISTINCT user FROM Administrator admin JOIN admin.user user WHERE admin.page.id IN "
			+ "(SELECT ipa.page.id FROM Administrator ipa WHERE ipa.user.id = :user_id) AND user.id != :user_id")
	public List<User> getContacts(@Param("user_id") Long user_id);
}
