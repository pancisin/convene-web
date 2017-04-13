package com.pancisin.employger.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;

import com.pancisin.employger.models.Duty;
import com.pancisin.employger.models.DutyClause;

public interface DutyClauseRepository extends JpaRepository<DutyClause, Long> {
	public DutyClause findByPrimaryDate(@Temporal Date primary_date);
	
	@Query("SELECT clause FROM DutyClause clause WHERE clause.primaryDate = :primary_date AND clause.duty = :duty")
	public DutyClause find(@Param("duty") Duty duty, @Param("primary_date") @Temporal Date primary_date);
}
