package com.pancisin.bookster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.models.Invitation;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

}
