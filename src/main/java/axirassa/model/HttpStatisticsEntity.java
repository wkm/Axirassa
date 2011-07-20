
package axirassa.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import org.hornetq.api.core.SimpleString;

import axirassa.util.AutoSerializingObject;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class HttpStatisticsEntity extends AutoSerializingObject implements Serializable {
	private static final long serialVersionUID = 8808442777520544095L;

	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Getter
	@Setter
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Date timestamp;

	@Getter
	@Setter
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private PingerEntity pinger;

	@Getter
	@Setter
	@Basic(optional = false)
	private int statusCode;

	@Getter
	@Setter
	@Basic(optional = false)
	private int latency;

	@Getter
	@Setter
	@Basic(optional = false)
	private int responseTime;

	@Getter
	@Setter
	@Basic(optional = true)
	private long responseSize;

	@Getter
	@Setter
	@Basic(optional = true)
	private long uncompressedSize;


	public long getTimestampInMillis() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getTimestamp());
		return calendar.getTimeInMillis();
	}


	public String getBroadcastAddress() {
		return PingerEntity.createBroadcastQueueName(getPinger().getUser().getId(), getPinger().getId());
	}
}
