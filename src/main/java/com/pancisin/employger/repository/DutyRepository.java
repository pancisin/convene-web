package com.pancisin.employger.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.employger.models.Company;
import com.pancisin.employger.models.Duty;

public interface DutyRepository extends JpaRepository<Duty, Long> {
	List<Duty> findByCompany(Company company);
}
