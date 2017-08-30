package com.pancisin.bookster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.models.Survey;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

}
