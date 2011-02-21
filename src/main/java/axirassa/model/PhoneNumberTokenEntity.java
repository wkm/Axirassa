
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import axirassa.model.interceptor.EntityPreSave;
import axirassa.util.RandomStringGenerator;

@Entity
@Table(name = "PhoneNumberTokens")
public class PhoneNumberTokenEntity implements Serializable, EntityPreSave {
	private static final long serialVersionUID = -8188942657861850878L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	@ManyToOne(optional = false)
	private UserPhoneNumberEntity phoneNumber;


	public UserPhoneNumberEntity getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(UserPhoneNumberEntity phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	@Basic(optional = false)
	private String token;


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiration;


	@Override
	public void preSave() {
		if (token == null)
			token = RandomStringGenerator.makeRandomStringToken(8);

		if (expiration == null)
			expiration = createExpiration();
	}


	private Date createExpiration() {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR, 24);
		return now.getTime();
	}

}
