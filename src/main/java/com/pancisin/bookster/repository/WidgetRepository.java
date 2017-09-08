package com.pancisin.bookster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.models.Widget;

public interface WidgetRepository extends JpaRepository<Widget, Long> {

}
