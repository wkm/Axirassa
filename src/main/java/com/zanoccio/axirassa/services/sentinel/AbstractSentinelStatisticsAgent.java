
package com.zanoccio.axirassa.services.sentinel;

import java.util.Date;

import org.hyperic.sigar.Sigar;

public abstract class AbstractSentinelStatisticsAgent implements SentinelAgent {

	private int machineid;
	private Sigar sigar;
	private Date date;


	@Override
	public String agentName() {
		return getClass().getSimpleName();
	}


	public void setMachineID(int machineid) {
		this.machineid = machineid;
	}


	public int getMachineID() {
		return machineid;
	}


	public void setSigar(Sigar sigar) {
		this.sigar = sigar;
	}


	public Sigar getSigar() {
		return sigar;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public Date getDate() {
		return date;
	}
}
