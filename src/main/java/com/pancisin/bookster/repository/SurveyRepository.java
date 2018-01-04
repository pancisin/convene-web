package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.model.Survey;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

	@Query("SELECT survey FROM Conference conference JOIN conference.surveys survey LEFT JOIN survey.submissions submission WHERE conference.id = :conference_id AND (submission IS NULL OR submission.user.id != :user_id) AND survey.state = 'IN_PROGRESS'")
	public List<Survey> getByConference(@Param("conference_id") Long conference_id, @Param("user_id") Long user_id);
}
