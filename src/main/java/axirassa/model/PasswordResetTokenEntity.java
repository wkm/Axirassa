
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

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import axirassa.model.interceptor.EntityPreSave;
import axirassa.util.RandomStringGenerator;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PasswordResetTokenEntity implements Serializable, EntityPreSave {
	private static final long serialVersionUID = -3839405383706605089L;

	@Id
	@Getter
	@Setter
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Getter
	@Setter
	@Basic(optional = false)
	private String token;


	@Getter
	@Setter
	@ManyToOne(optional = false)
	private UserEntity user;


	@Getter
	@Setter
	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiration;



	private Date createExpiration () {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR, 24);
		return now.getTime();
	}


	@Override
	public void preSave () {
		if (token == null)
			token = RandomStringGenerator.getInstance().randomStringToken(64);

		if (expiration == null)
			expiration = createExpiration();
	}
}
