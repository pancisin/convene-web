package com.pancisin.bookster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.models.Locale;

public interface LocaleRepository extends JpaRepository<Locale, Long> {

	public Locale findByCode(String code);
}
