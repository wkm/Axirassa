
package axirassa.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import axirassa.model.interceptor.EntityPreSave;
import axirassa.util.AutoSerializingObject;
import axirassa.util.RandomStringGenerator;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserPhoneNumberEntity extends AutoSerializingObject implements Serializable, EntityPreSave, EntityWithUser {
	private static final long serialVersionUID = 1344815747977623929L;

	//
	// Instance
	//

	@Id
	@Getter
	@Setter
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	@Getter
	@Setter
	@ManyToOne(optional = false)
	private UserEntity user;


	@Getter
	@Setter
	@Basic(optional = false)
	private String phoneNumber;



	public String getFormattedPhoneNumber () {
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

	@Getter
	@Setter
	@Basic(optional = true)
	private String extension;


	@Getter
	@Setter
	@Basic(optional = false)
	private boolean confirmed;


	@Getter
	@Setter
	@Basic(optional = false)
	private boolean acceptingVoice;


	@Getter
	@Setter
	@Basic(optional = false)
	private boolean acceptingSms ;


	@Getter
	@Setter
	@Basic(optional = false)
	private String token;


	public String getFormattedToken () {
		StringBuilder sb = new StringBuilder();

		int spanLength = 2;
		for (int i = 0; i < token.length(); i += spanLength) {
			if (i != 0)
				sb.append("-");

			sb.append(token.substring(i, i + spanLength));
		}

		return sb.toString();
	}


	public String createToken () {
		String tokenStr = RandomStringGenerator.makeRandomStringToken(8);
		return tokenStr.toUpperCase();
	}


	@Override
	public void preSave () {
		if (token == null)
			token = createToken();
	}

}
