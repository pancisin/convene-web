package com.pancisin.bookster.repository;

import com.pancisin.bookster.model.Form;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FormRepository extends JpaRepository<Form, UUID> {

}
