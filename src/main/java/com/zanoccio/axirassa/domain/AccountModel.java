
package com.zanoccio.axirassa.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Accounts")
public class AccountModel implements Serializable {
	private static final long serialVersionUID = -6937561064726878987L;

	// an account contains users
}
