package com.pancisin.bookster.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.model.ServiceRequest;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

	@Query("SELECT request FROM ServiceRequest request JOIN request.service service JOIN service.page page WHERE page.id = :page_id")
	public Page<ServiceRequest> getByPage(@Param("page_id") Long page_id, Pageable pageable);
}
