
package com.zanoccio.axirassa.services.sentinel;

import org.hibernate.Session;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.SigarException;

public class MemorySentinelAgent extends AbstractSentinelAgent {

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

}
