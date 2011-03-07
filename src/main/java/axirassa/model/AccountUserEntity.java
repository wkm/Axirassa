
package axirassa.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class AccountUserEntity implements Serializable {
	private static final long serialVersionUID = -1634641083458982998L;

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
	private AccountEntity account;


	@OneToOne(targetEntity = AccountEntity.class, fetch = FetchType.LAZY)
	@Cascade({ CascadeType.PERSIST })
	public AccountEntity getAccount() {
		return account;
	}


	public void setAccount(AccountEntity account) {
		this.account = account;
	}


	// USER
	private UserEntity user;


	@OneToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
	@Cascade({ CascadeType.PERSIST })
	public UserEntity getUser() {
		return user;
	}


	public void setUser(UserEntity user) {
		this.user = user;
	}


	// ROLE
	private UserRole role;


	@Basic(optional = false)
	@Enumerated(EnumType.STRING)
	public UserRole getRole() {
		return role;
	}


	public void setRole(UserRole role) {
		this.role = role;
	}
}
