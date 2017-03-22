package com.pancisin.employger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.employger.models.Duty;

public interface DutyRepository extends JpaRepository<Duty, Long> {
}
