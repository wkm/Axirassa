
package com.zanoccio.axirassa.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "AccountUsers")
public class AccountUserModel {

	// ID
	private Long id;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getid() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	// ACCOUNT
	private AccountModel account;


	@OneToOne(targetEntity = AccountModel.class, fetch = FetchType.LAZY)
	@Cascade({ CascadeType.PERSIST })
	public AccountModel getAccount() {
		return account;
	}


	public void setAccount(AccountModel account) {
		this.account = account;
	}


	// USER
	private UserModel user;


	@OneToOne(targetEntity = UserModel.class, fetch = FetchType.LAZY)
	@Cascade({ CascadeType.PERSIST })
	public UserModel getUser() {
		return user;
	}


	public void setUser(UserModel user) {
		this.user = user;
	}


	// ROLE
	private UserRole role;


	@Enumerated(EnumType.STRING)
	public UserRole getRole() {
		return role;
	}


	public void setRole(UserRole role) {
		this.role = role;
	}
}
