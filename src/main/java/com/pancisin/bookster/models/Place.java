package com.pancisin.bookster.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "places")
public class Place {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true)
	private String facebookId;
	
	@Column
	private String name;
	
	@Column
	private int capacity;
	
	@Column
	private String description;
	
	@JsonManagedReference
	@OneToOne(cascade = CascadeType.ALL)
	private Address address;

	@Column
	@JsonProperty(access = Access.READ_ONLY)
	private String venueJsonUrl;
	
	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private String venueData;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST })
	private List<Media> gallery;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public String getVenueJsonUrl() {
		return venueJsonUrl;
	}

	public void setVenueJsonUrl(String venueJsonUrl) {
		this.venueJsonUrl = venueJsonUrl;
	}

	public void setVenueData(String venueData) {
		this.venueData = venueData;
	}

	public String getVenueData() {
		return venueData;
	}

	public List<Media> getGallery() {
		return gallery;
	}

	public void AddGallery(Media media) {
		if (this.gallery == null) {
			this.gallery = new ArrayList<Media>();
		}
		
		this.gallery.add(media);
	}
}
