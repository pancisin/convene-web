package com.pancisin.employger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.employger.models.Programme;

public interface ProgrammeRepository extends JpaRepository<Programme, Long> {

}
