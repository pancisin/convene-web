package com.pancisin.bookster.models;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pancisin.bookster.model.*;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.pancisin.bookster.model.enums.Locale;
import com.pancisin.bookster.model.enums.Role;
import com.pancisin.bookster.model.enums.Subscription;
import com.pancisin.bookster.model.enums.SubscriptionState;
import com.pancisin.bookster.model.interfaces.IAuthor;
import com.pancisin.bookster.models.views.Compact;
import com.pancisin.bookster.models.views.Summary;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Indexed
@Table(name = "users")
public class User implements UserDetails, Principal, IAuthor {

	private static final long serialVersionUID = -2205856327940777873L;

	@Id
	@JsonView(Compact.class)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Field
	@JsonView(Compact.class)
	@Column(name = "first_name")
	private String firstName;

	@NotNull
	@Field
	@JsonView(Compact.class)
	@Column(name = "last_name")
	private String lastName;

	@NotNull
	@Email
	@Field
	@JsonView(Compact.class)
	@Column(unique = true)
	private String email;

	@Size(min = 6, max = 30)
	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	@JsonIgnore
	@Column(name = "password")
	private String hashedPassword;

	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private String passwordConfirm;

	@Transient
	@JsonProperty(access = Access.READ_ONLY)
	private String token;

	@JsonIgnore
	@Column(name = "locked")
	private boolean locked;

	@Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar created;

	@Transient
	private Collection<? extends GrantedAuthority> authorities;

	@JsonIgnore
	@ManyToMany(mappedBy = "followers")
	private List<Page> followed;

	public User(Long id, String email, String token, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.email = email;
		this.token = token;
		this.authorities = authorities;
	}

	public User() {
	}

	@JsonIgnore
	@OneToMany(mappedBy = "owner")
	private List<Event> events;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<UserSubscription> subscriptions = new ArrayList<UserSubscription>();

	@NotNull
	@JsonView(Summary.class)
	@Enumerated(EnumType.STRING)
	private Role role = Role.ROLE_VISITOR;

	@Enumerated(EnumType.STRING)
	@JsonView(Summary.class)
	private Locale locale;

	@Column
	private boolean verified = false;

	@Transient
	public UserSubscription getLicense() {
		Optional<UserSubscription> subscription = subscriptions.stream()
				.filter(s -> s.getState() == SubscriptionState.ACTIVE || s.getState() == SubscriptionState.NEW).findFirst();

		if (subscription.isPresent())
			return subscription.get();
		else {
			UserSubscription free = new UserSubscription();
			free.setSubscription(Subscription.FREE);
			free.setState(SubscriptionState.ACTIVE);
			return free;
		}
	}

	@OneToOne(optional = true, cascade = CascadeType.ALL, orphanRemoval = true)
	private Address address = new Address();

	@JsonProperty(access = Access.READ_ONLY)
	@OneToOne(optional = true, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH })
	@JsonView(Summary.class)
	private Media profilePicture;

	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private String profilePictureData;

  @ElementCollection
  @Lob
  @MapKeyColumn(name = "meta_key")
  @Column(name = "meta_value")
  @CollectionTable(name = "users_metadata")
	@JsonView(Summary.class)
  private Map<String, String> metadata = new HashMap<String, String>();

	@JsonProperty(access = Access.READ_ONLY)
	private Long facebookId;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return this.firstName + " " + this.lastName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String last_name) {
		this.lastName = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public Calendar getCreated() {
		return created;
	}

	public List<Event> getEvents() {
		return events;
	}

	@Override
	public String getName() {
		return this.email;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public List<UserSubscription> getSubscriptions() {
		return subscriptions;
	}

	@Override
	public String getDisplayName() {
		return this.firstName + " " + this.lastName;
	}

	@Override
	public String getType() {
		return "user";
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public List<Page> getFollowed() {
		return followed;
	}

	public Media getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(Media profilePicture) {
		this.profilePicture = profilePicture;
	}

	public void setProfilePictureData(String profilePictureData) {
		this.profilePictureData = profilePictureData;
	}

	public String getProfilePictureData() {
		return profilePictureData;
	}

	public Long getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(Long facebookId) {
		this.facebookId = facebookId;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}
}
