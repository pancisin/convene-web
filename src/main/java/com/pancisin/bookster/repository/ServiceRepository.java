package com.pancisin.bookster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.model.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {

}
