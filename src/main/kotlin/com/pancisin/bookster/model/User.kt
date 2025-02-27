package com.pancisin.bookster.model

import java.security.Principal
import java.util.ArrayList
import java.util.Calendar
import java.util.HashMap

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

import org.hibernate.search.annotations.Field
import org.hibernate.search.annotations.Indexed
import org.hibernate.validator.constraints.Email
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access
import com.pancisin.bookster.model.interfaces.IAuthor
import com.pancisin.bookster.models.views.Compact
import com.pancisin.bookster.models.views.Summary
import com.fasterxml.jackson.annotation.JsonView
import com.pancisin.bookster.model.enums.*
import javax.persistence.*

@Entity
@Indexed
@Table(name = "users")
class User() : UserDetails, Principal {

  @Id
  @JsonView(Compact::class)
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null

  @NotNull
  @Field
  @JsonView(Compact::class)
  @Column(name = "first_name")
  var firstName: String? = null

  @NotNull
  @Field
  @JsonView(Compact::class)
  @Column(name = "last_name")
  var lastName: String? = null

  @NotNull
  @Email
  @Field
  @JsonView(Compact::class)
  @Column(unique = true)
  var email: String? = null

  @Size(min = 6, max = 30)
  @Transient
  @JsonProperty(access = Access.WRITE_ONLY)
  private var password: String? = null

  @JsonIgnore
  @Column(name = "password")
  var hashedPassword: String? = null

  @Transient
  @JsonProperty(access = Access.READ_ONLY)
  var token: String? = null

  @JsonIgnore
  @Column(name = "locked")
  var isLocked: Boolean = false

  @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  val created: Calendar? = null

  @Transient
  private var authorities: Collection<GrantedAuthority> = ArrayList()

  @JsonIgnore
  @OneToMany(mappedBy = "owner")
  val events: List<Event>? = null

  @JsonIgnore
  @OneToMany(mappedBy = "user")
  val subscriptions: List<UserSubscription> = ArrayList()

  @NotNull
  @JsonView(Summary::class)
  @Enumerated(EnumType.STRING)
  @JsonProperty(access = Access.READ_ONLY)
  var role = Role.ROLE_VISITOR

  @Enumerated(EnumType.STRING)
  @JsonView(Summary::class)
  @JsonProperty(access = Access.READ_ONLY)
  var locale: Locale? = null

  @Column
  var verified = false

  val license: UserSubscription
    @Transient
    get() = subscriptions.firstOrNull { s -> s.state === SubscriptionState.ACTIVE || s.state === SubscriptionState.NEW }
      ?: UserSubscription(subscription = Subscription.FREE, state = SubscriptionState.ACTIVE)

  @OneToOne(optional = true, cascade = arrayOf(CascadeType.ALL), orphanRemoval = true)
  var address = Address()

  @JsonProperty(access = Access.READ_ONLY)
  @OneToOne(optional = true, cascade = arrayOf(CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH))
  @JsonView(Summary::class)
  var profilePicture: Media? = null

  @Transient
  @JsonProperty(access = Access.WRITE_ONLY)
  var profilePictureData: String? = null

  @ElementCollection
  @Lob
  @MapKeyColumn(name = "meta_key")
  @Column(name = "meta_value")
  @CollectionTable(name = "users_metadata")
  @JsonView(Summary::class)
  var metadata: Map<String, String> = HashMap()

  @JsonProperty(access = Access.READ_ONLY)
  var facebookId: Long? = null

  @ElementCollection
  @MapKeyColumn(name = "constraint_key")
  @Column(name = "access")
  @CollectionTable(name = "users_privacy_constraints")
  @Enumerated(EnumType.STRING)
  @JsonIgnore
  var privacyConstraints: MutableMap<String, PrivacyAccess> = HashMap()

  constructor(id: Long?, email: String, token: String, authorities: Collection<GrantedAuthority>) : this() {
    this.id = id
    this.email = email
    this.token = token
    this.authorities = authorities
  }

  val displayName: String
    get() = this.firstName + " " + this.lastName

  override fun getAuthorities(): Collection<GrantedAuthority> {
    return authorities
  }

  override fun getUsername(): String? {
    return email
  }

  override fun isAccountNonExpired(): Boolean {
    return true
  }

  override fun isAccountNonLocked(): Boolean {
    return !isLocked
  }

  override fun isCredentialsNonExpired(): Boolean {
    return true
  }

  override fun isEnabled(): Boolean {
    return true
  }

  override fun toString(): String {
    return this.firstName + " " + this.lastName
  }

  override fun getPassword(): String? {
    return password
  }

  override fun getName(): String? {
    return this.email
  }
}
