package com.pancisin.employger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.employger.models.Employees;

public interface EmployeeRepository extends JpaRepository<Employees, Long> {

}
