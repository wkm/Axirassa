
package axirassa.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import axirassa.model.interceptor.EntityPreSave;
import axirassa.util.RandomStringGenerator;

@Entity
public class PasswordResetTokenEntity implements Serializable, EntityPreSave {
	private static final long serialVersionUID = -3839405383706605089L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;


	public void setId(Long id) {
		this.id = id;
	}


	public Long getId() {
		return id;
	}


	@Basic(optional = false)
	private String token;


	public void setToken(String token) {
		this.token = token;
	}


	public String getToken() {
		return token;
	}


	@ManyToOne(optional = false)
	private UserEntity user;


	public UserEntity getUser() {
		return user;
	}


	public void setUser(UserEntity user) {
		this.user = user;
	}


	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiration;


	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}


	public Date getExpiration() {
		return expiration;
	}


	private Date createExpiration() {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR, 24);
		return now.getTime();
	}


	@Override
	public void preSave() {
		if (token == null)
			token = RandomStringGenerator.getInstance().randomStringToken(64);

		if (expiration == null)
			expiration = createExpiration();
	}
}
