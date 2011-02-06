
package axirassa.services.sentinel;

import java.util.Collection;
import java.util.Collections;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.SigarException;

import axirassa.services.sentinel.model.SentinelMemoryStatisticEntity;

public class MemorySentinelAgent extends AbstractSentinelStatisticsAgent {

	private SentinelMemoryStatisticEntity memorystat;


	@Override
	public void execute() throws SigarException {
		Mem mem = getSigar().getMem();

		memorystat = new SentinelMemoryStatisticEntity();
		memorystat.machineid = getMachineID();
		memorystat.date = getDate();
		memorystat.used = mem.getActualUsed();
		memorystat.total = mem.getTotal();
	}


	@Override
	public Collection<SentinelMemoryStatisticEntity> getStatistics() {
		return Collections.singleton(memorystat);
	}
}
