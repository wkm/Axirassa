
package axirassa.services.sentinel;

import java.util.Collection;
import java.util.Date;

import org.hibernate.Session;
import org.hyperic.sigar.Sigar;

public abstract class AbstractSentinelStatisticsAgent implements SentinelAgent {

	private int machineid;
	private Sigar sigar;
	private Date date;


	@Override
	public String agentName() {
		return getClass().getSimpleName();
	}


	@Override
	public void setMachineID(final int machineid) {
		this.machineid = machineid;
	}


	@Override
	public int getMachineID() {
		return machineid;
	}


	@Override
	public void setSigar(final Sigar sigar) {
		this.sigar = sigar;
	}


	@Override
	public Sigar getSigar() {
		return sigar;
	}


	@Override
	public void setDate(final Date date) {
		this.date = date;
	}


	@Override
	public Date getDate() {
		return date;
	}


	abstract public Collection<? extends SentinelStatistic> getStatistics();


	@Override
	public void save(final Session session) {
		for (SentinelStatistic statistic : getStatistics())
			statistic.save(session);
	}
}
