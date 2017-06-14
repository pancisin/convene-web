package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pancisin.bookster.models.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

	public Place findByFacebookId(String facebookId);
}
