
package com.zanoccio.axirassa.services.sentinel;

import java.util.Collection;
import java.util.Collections;

import org.hibernate.Session;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.SigarException;

public class MemorySentinelAgent extends AbstractSentinelStatisticsAgent {

	private MemoryStatistic memorystat;


	@Override
	public void execute() throws SigarException {
		Mem mem = getSigar().getMem();
		memorystat = new MemoryStatistic(getMachineID(), getDate(), mem.getActualUsed(), mem.getTotal());
	}


	@Override
	public void save(Session session) {
		memorystat.save(session);
	}


	@Override
	public Collection<MemoryStatistic> getStatistics() {
		return Collections.singleton(memorystat);
	}
}
