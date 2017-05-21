package com.pancisin.bookster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.PageAdministrator;
import com.pancisin.bookster.models.User;

public interface PageAdministratorRepository extends JpaRepository<PageAdministrator, Long> {

//	@Query("SELECT user FROM pageAdministrator pa JOIN pageAdministrator.user user WHERE pa.role = 'ROLE_OWNER'")
//	public User getPageOwner(Page page);
}
