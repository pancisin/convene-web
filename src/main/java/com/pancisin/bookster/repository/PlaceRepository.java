package com.pancisin.bookster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.models.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {

}
