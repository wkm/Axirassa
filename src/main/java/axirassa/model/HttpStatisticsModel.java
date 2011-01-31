
package axirassa.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "HttpStatistics")
public class HttpStatisticsModel implements Serializable {
	private static final long serialVersionUID = 8808442777520544095L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private PingerModel pinger;


	public void setPinger(PingerModel pinger) {
		this.pinger = pinger;
	}


	public PingerModel getPinger() {
		return pinger;
	}


	@Basic(optional = false)
	private int statusCode;


	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}


	public int getStatusCode() {
		return statusCode;
	}


	@Basic(optional = false)
	private int latency;


	public void setLatency(int latency) {
		this.latency = latency;
	}


	public int getLatency() {
		return latency;
	}


	@Basic(optional = false)
	private int responseTime;


	public void setResponseTime(int responseTime) {
		this.responseTime = responseTime;
	}


	public int getResponseTime() {
		return responseTime;
	}


	@Basic(optional = true)
	private long responseSize;


	public void setResponseSize(long responseSize) {
		this.responseSize = responseSize;
	}


	public long getResponseSize() {
		return responseSize;
	}


	@Basic(optional = true)
	private long uncompressedSize;


	public void setUncompressedSize(long uncompressedSize) {
		this.uncompressedSize = uncompressedSize;
	}


	public long getUncompressedSize() {
		return uncompressedSize;
	}

}
