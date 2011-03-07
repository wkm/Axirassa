
package axirassa.model;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.Query;
import org.hibernate.Session;

import axirassa.model.interceptor.EntityPreSave;
import axirassa.util.AutoSerializingObject;
import axirassa.util.RandomStringGenerator;

@Entity
public class UserEmailAddressEntity extends AutoSerializingObject implements EntityPreSave, EntityWithUser {
	private static final long serialVersionUID = 4776151084882073597L;


	//
	// Static
	//

	public static UserEmailAddressEntity getByIdWithUser(Session database, long id) {
		Query query = database.getNamedQuery("email_by_id");
		query.setLong("id", id);

		List<UserEmailAddressEntity> emails = query.list();
		if (emails.size() > 0)
			return emails.iterator().next();
		else
			return null;
	}


	public static List<UserEmailAddressEntity> getEmailsByUser(Session database, UserEntity user) {
		Query query = database.getNamedQuery("user_emails");
		query.setEntity("user", user);
		return query.list();
	}


	public static UserEmailAddressEntity getByToken(Session database, String token) {
		Query query = database.getNamedQuery("email_by_token");
		query.setString("token", token);

		List<UserEmailAddressEntity> email = query.list();
		if (email.size() == 0)
			return null;
		else
			return email.iterator().next();
	}


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


	@Basic(optional = false)
	private boolean verified;


	public boolean isVerified() {
		return verified;
	}


	public void setVerified(boolean verified) {
		this.verified = verified;
	}


	@Override
	public void preSave() {
		if (token == null)
			token = RandomStringGenerator.makeRandomStringToken(36);
	}
}
