package com.pancisin.employger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.employger.models.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
