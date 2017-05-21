package com.pancisin.bookster.models;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pancisin.bookster.models.enums.Subscription;
import com.pancisin.bookster.models.enums.SubscriptionState;

@Entity
@Table(name = "users_subscriptions")
public class UserSubscription {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonIgnore
	@ManyToOne(optional = false)
	private User user;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar acquired;

	@Column(updatable = false)
	private Calendar expires;

	@Enumerated(EnumType.STRING)
	private Subscription subscription;

	@Enumerated(EnumType.STRING)
	private SubscriptionState state = SubscriptionState.NEW;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Calendar getExpires() {
		return expires;
	}

	public void setExpires(Calendar expires) {
		this.expires = expires;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public SubscriptionState getState() {
		return state;
	}

	public void setState(SubscriptionState state) {
		this.state = state;
	}

	public Long getId() {
		return id;
	}

	public Calendar getAcquired() {
		return acquired;
	}
}
