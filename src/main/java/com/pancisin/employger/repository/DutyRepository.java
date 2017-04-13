package com.pancisin.employger.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pancisin.employger.models.Company;
import com.pancisin.employger.models.Duty;
import com.pancisin.employger.repository.implementations.DutyCustomRepository;

@Repository
public interface DutyRepository extends JpaRepository<Duty, Long>, DutyCustomRepository {
	List<Duty> findByCompany(Company company);
}
