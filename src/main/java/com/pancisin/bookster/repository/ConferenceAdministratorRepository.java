package com.pancisin.bookster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pancisin.bookster.models.ConferenceAdministrator;

@Repository
public interface ConferenceAdministratorRepository extends JpaRepository<ConferenceAdministrator, Long> {

}
