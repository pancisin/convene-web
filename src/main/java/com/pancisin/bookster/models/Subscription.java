package com.pancisin.bookster.models;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.pancisin.bookster.models.enums.SubscriptionState;

public class Subscription {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private SubscriptionState state = SubscriptionState.NEW;
	
	@Column
	private Calendar acquired;
	
	@Column
	private Calendar expires;
}
