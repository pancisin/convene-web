package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.model.Survey;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

	@Query("SELECT survey FROM Page page JOIN page.surveys survey LEFT JOIN survey.form.submissions submission WHERE page.id = :page_id AND (submission IS NULL OR submission.user.id != :user_id) AND survey.state = 'IN_PROGRESS'")
	public List<Survey> getByPage(@Param("page_id") Long page_id, @Param("user_id") Long user_id);
}
