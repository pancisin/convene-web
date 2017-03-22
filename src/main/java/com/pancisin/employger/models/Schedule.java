package com.pancisin.employger.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "schedules")
public class Schedule {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
//	@Column(name = "interval")
//	private Timestamp interval;
//
//	public Timestamp getInterval() {
//		return interval;
//	}
//
//	public void setInterval(Timestamp interval) {
//		this.interval = interval;
//	}
//
//	public Long getId() {
//		return id;
//	}
}
