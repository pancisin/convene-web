package com.pancisin.bookster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.model.FormSubmission;

public interface FormSubmissionRepository extends JpaRepository<FormSubmission, Long> {

}
