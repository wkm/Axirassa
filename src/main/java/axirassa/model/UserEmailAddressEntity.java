
package axirassa.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import axirassa.model.interceptor.EntityPreSave;
import axirassa.util.AutoSerializingObject;
import axirassa.util.RandomStringGenerator;

@Entity
public class UserEmailAddressEntity extends AutoSerializingObject implements EntityPreSave, EntityWithUser {
	private static final long serialVersionUID = 4776151084882073597L;

	//
	// Instance
	//
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	@ManyToOne(optional = false)
	private UserEntity user;


	@Override
	public UserEntity getUser() {
		return user;
	}


	public void setUser(UserEntity user) {
		this.user = user;
	}


	@Basic(optional = false)
	private String email;


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	@Basic(optional = false)
	private String token;


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	@Override
	public void preSave() {
		if (token == null)
			token = RandomStringGenerator.makeRandomStringToken(36);
	}
}
