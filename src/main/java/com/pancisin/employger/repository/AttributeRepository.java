package com.pancisin.employger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.employger.models.Attribute;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {

}
