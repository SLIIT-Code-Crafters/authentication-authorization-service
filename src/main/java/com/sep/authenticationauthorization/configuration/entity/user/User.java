package com.sep.authenticationauthorization.configuration.entity.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sep.authenticationauthorization.configuration.enums.Gender;
import com.sep.authenticationauthorization.configuration.enums.Roles;
import com.sep.authenticationauthorization.configuration.enums.Salutation;
import com.sep.authenticationauthorization.configuration.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "users")
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "user_name", nullable = false, unique = true)
	private String userName;

	@Column(name = "nic")
	private String nic;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender")
	private Gender gender;

	@Enumerated(EnumType.STRING)
	@Column(name = "salutation")
	private Salutation salutation;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@Column(name = "contact_no", nullable = false)
	private String contactNo;

	@Column(name = "address_line_1", nullable = true)
	private String addressLine1;

	@Column(name = "address_line_2", nullable = true)
	private String addressLine2;

	@Column(name = "address_line_3", nullable = true)
	private String addressLine3;

	@Column(name = "password", nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	private Roles role;

	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "updated_date")
	private LocalDateTime updatedDate;

	@Transient
	private String masterToken;

	@Transient
	private String authToken;

//	@ManyToMany
//	@LazyCollection(LazyCollectionOption.FALSE)
//	private Set<Role> role;

	public String getOriginalUsername() {
		return userName;
	}

//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		Set<GrantedAuthority> authorities = new HashSet<>();
//		for (Role userRole : role) {
//			authorities.add(new SimpleGrantedAuthority(userRole.getName().name()));
//		}
//		return authorities;
//	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
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
		return true;
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
	public String getPassword() {
		return password;
	}

}
