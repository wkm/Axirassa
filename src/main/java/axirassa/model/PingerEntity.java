
package axirassa.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import axirassa.util.AutoSerializingObject;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PingerEntity extends AutoSerializingObject implements Serializable, EntityWithUser {
	private static final long serialVersionUID = -6709719920544228167L;


	public static String createBroadcastQueueName (Long userId, Long pingerId) {
		return "ax.account." + userId + ".pinger." + pingerId;
	}


	//
	// INSTANCE
	//

	// ID
	@Id
	@Getter
	@Setter
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	// USER
	@Getter
	@Setter
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private UserEntity user;


	// URL
	@Getter
	@Setter
	@Basic(optional = false)
	private String url;


	/**
	 * The number of seconds between checks
	 */
	@Getter
	@Basic(optional = false)
	private int frequency; 

	public void setFrequency(int frequency) {
		this.frequency= frequency;
	}

	public void setFrequency (PingerFrequency frequency) {
		this.frequency = frequency.getInterval();
	}


	@Getter
	@Setter
	@Basic(optional = false)
	@Enumerated(EnumType.STRING)
	public MonitorType monitorType;
}
