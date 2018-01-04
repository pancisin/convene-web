package com.pancisin.bookster.model

import java.util.Calendar

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

import org.hibernate.annotations.GenericGenerator

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access
import com.pancisin.bookster.models.User
import com.pancisin.bookster.models.enums.Subscription
import com.pancisin.bookster.models.enums.SubscriptionState

@Entity
@Table(name = "users_subscriptions")
data class UserSubscription(
  @Id @GenericGenerator(name = "invoice_number_generator", strategy = "com.pancisin.bookster.utils.InvoiceNumberGenerator")
  @GeneratedValue(generator = "invoice_number_generator") @Column(updatable = false, nullable = false)
  val id: String? = null,

  @JsonProperty(access = Access.WRITE_ONLY) @ManyToOne(optional = false)
  var user: User? = null,

  @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  val acquired: Calendar? = null,

  @Column(updatable = false)
  var expires: Calendar? = null,

  @Enumerated(EnumType.STRING)
  var subscription: Subscription? = null,

  @Enumerated(EnumType.STRING)
  var state: SubscriptionState = SubscriptionState.NEW,

  var idSale: Long? = null
)
