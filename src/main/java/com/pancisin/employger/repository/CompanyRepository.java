package com.pancisin.employger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.employger.models.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
	Company findByIco(String ico);
}
