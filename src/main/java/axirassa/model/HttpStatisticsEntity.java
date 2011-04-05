
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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import axirassa.util.AutoSerializingObject;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class HttpStatisticsEntity extends AutoSerializingObject implements Serializable {
	private static final long serialVersionUID = 8808442777520544095L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Date timestamp;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private PingerEntity pinger;


	public void setPinger (PingerEntity pinger) {
		this.pinger = pinger;
	}


	public PingerEntity getPinger () {
		return pinger;
	}


	@Basic(optional = false)
	private int statusCode;


	public void setStatusCode (int statusCode) {
		this.statusCode = statusCode;
	}


	public int getStatusCode () {
		return statusCode;
	}


	@Basic(optional = false)
	private int latency;


	public void setLatency (int latency) {
		this.latency = latency;
	}


	public int getLatency () {
		return latency;
	}


	@Basic(optional = false)
	private int responseTime;


	public void setResponseTime (int responseTime) {
		this.responseTime = responseTime;
	}


	public int getResponseTime () {
		return responseTime;
	}


	@Basic(optional = true)
	private long responseSize;


	public void setResponseSize (long responseSize) {
		this.responseSize = responseSize;
	}


	public long getResponseSize () {
		return responseSize;
	}


	@Basic(optional = true)
	private long uncompressedSize;


	public void setUncompressedSize (long uncompressedSize) {
		this.uncompressedSize = uncompressedSize;
	}


	public long getUncompressedSize () {
		return uncompressedSize;
	}


	public void setTimestamp (Date timestamp) {
		this.timestamp = timestamp;
	}


	public Date getTimestamp () {
		return timestamp;
	}


	public long getTimestampInMillis () {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getTimestamp());
		return calendar.getTimeInMillis();
	}
}
