package com.pancisin.bookster.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.model.FormSubmission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FormSubmissionRepository extends JpaRepository<FormSubmission, Long> {

  @Query("SELECT submission FROM Service service JOIN service.form form JOIN form.submissions submission WHERE service.page.id = :page_id")
  List<FormSubmission> getServiceRequestsByPage(@Param("page_id") Long page_id);
}
