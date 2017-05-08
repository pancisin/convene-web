package com.pancisin.bookster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.models.Programme;

public interface ProgrammeRepository extends JpaRepository<Programme, Long> {

}
