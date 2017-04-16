package com.pancisin.employger.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pancisin.employger.models.Company;
import com.pancisin.employger.models.Duty;
import com.pancisin.employger.repository.implementations.DutyCustomRepository;
import com.pancisin.employger.rest.controllers.objects.DutyInstance;

@Repository
public interface DutyRepository extends JpaRepository<Duty, Long>, DutyCustomRepository {
	List<Duty> findByCompany(Company company);
}
