
package axirassa.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.Query;
import org.hibernate.Session;

import axirassa.util.AutoSerializingObject;

@Entity
@Table(name = "UserPhoneNumbers")
public class UserPhoneNumberEntity extends AutoSerializingObject implements Serializable {
	private static final long serialVersionUID = 1344815747977623929L;


	//
	// Static
	//

	public static List<UserPhoneNumberEntity> getPhoneNumbersByUser(Session session, UserEntity user) {
		Query query = session.getNamedQuery("user_phonenumbers");
		query.setEntity("user", user);

		return query.list();
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


	public UserEntity getUser() {
		return user;
	}


	public void setUser(UserEntity user) {
		this.user = user;
	}


	@Basic(optional = false)
	private String phoneNumber;


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getFormattedPhoneNumber() {
		StringBuilder formatted = new StringBuilder();

		formatted.append('(');
		formatted.append(phoneNumber.substring(0, 3));
		formatted.append(") ");
		formatted.append(phoneNumber.substring(3, 6));
		formatted.append("-");
		formatted.append(phoneNumber.substring(6));

		if (extension != null)
			formatted.append(" x").append(extension);

		return formatted.toString();
	}


	@Basic(optional = true)
	private String extension;


	public String getExtension() {
		return extension;
	}


	public void setExtension(String extension) {
		this.extension = extension;
	}


	@Basic(optional = false)
	private boolean confirmed = false;


	public boolean isConfirmed() {
		return confirmed;
	}


	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}


	@Basic(optional = false)
	private boolean acceptingVoice = false;


	public boolean isAcceptingVoice() {
		return acceptingVoice;
	}


	public void setAcceptingVoice(boolean acceptingVoice) {
		this.acceptingVoice = acceptingVoice;
	}


	@Basic(optional = false)
	private boolean acceptingSms = false;


	public boolean isAcceptingSms() {
		return acceptingSms;
	}


	public void setAcceptingSms(boolean acceptingSms) {
		this.acceptingSms = acceptingSms;
	}

}
