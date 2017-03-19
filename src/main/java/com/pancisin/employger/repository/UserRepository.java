package com.pancisin.employger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.employger.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
}
