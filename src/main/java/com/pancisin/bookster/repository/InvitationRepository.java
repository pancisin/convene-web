package com.pancisin.bookster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pancisin.bookster.models.Invitation;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

}
