
package com.zanoccio.axirassa.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Accounts")
public class AccountModel implements Serializable {
	private static final long serialVersionUID = -6937561064726878987L;

	// ID
	private Long id;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	// Name
	private String name;


	@Basic
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

}
