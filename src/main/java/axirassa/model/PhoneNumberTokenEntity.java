
package axirassa.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import axirassa.model.interceptor.EntityPreSave;
import axirassa.util.Meta;
import axirassa.util.RandomStringGenerator;

@Entity
@Table(name = "PhoneNumberTokens")
public class PhoneNumberTokenEntity implements Serializable, EntityPreSave {
	private static final long serialVersionUID = -8188942657861850878L;


	//
	// STATIC
	//
	public static List<PhoneNumberTokenEntity> getTokensByPhoneNumber(Session session,
	        UserPhoneNumberEntity phoneNumberEntity) {
		Query query = session.getNamedQuery("tokens_by_phoneNumber");
		Meta.inspect(phoneNumberEntity);
		query.setEntity("phoneNumber", phoneNumberEntity);
		return query.list();
	}


	//
	// INSTANCE
	//

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


	@Cascade({ CascadeType.DELETE })
	private UserPhoneNumberEntity phoneNumberEntity;


	public UserPhoneNumberEntity getPhoneNumberEntity() {
		return phoneNumberEntity;
	}


	public void setPhoneNumberEntity(UserPhoneNumberEntity phoneNumber) {
		this.phoneNumberEntity = phoneNumber;
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
