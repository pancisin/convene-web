package com.pancisin.bookster.model

import javax.persistence.Id
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal

@Entity
@Table(name = "addresses")
data class Address(

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	var id: Long? = null,

	@Column
	var state: String? = null,

	@Column
	var city: String? = null,

	@Column
	var street: String? = null,

	@Column
	var number: String? = null,

	@Column
	var zip: String? = null,

	@Column
	var formatted: String? = null,

	@Column(precision = 10, scale = 8)
	var latitude: BigDecimal? = null,

	@Column(precision = 10, scale = 8)
	var longitude: BigDecimal? = null
) {

	@JsonIgnore
	fun getPaylaneAddress() : com.paylane.client.api.models.Address {
		return com.paylane.client.api.models.Address( "${this.street} ${this.number}", this.city, this.state,
				this.zip, "SK");
	}
}
