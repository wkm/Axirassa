
package axirassa.services.sentinel;

import java.util.Collection;
import java.util.Collections;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.SigarException;

import axirassa.services.sentinel.model.SentinelMemoryStatisticModel;

public class MemorySentinelAgent extends AbstractSentinelStatisticsAgent {

	private SentinelMemoryStatisticModel memorystat;


	@Override
	public void execute() throws SigarException {
		Mem mem = getSigar().getMem();

		memorystat = new SentinelMemoryStatisticModel();
		memorystat.machineid = getMachineID();
		memorystat.date = getDate();
		memorystat.used = mem.getActualUsed();
		memorystat.total = mem.getTotal();
	}


	@Override
	public Collection<SentinelMemoryStatisticModel> getStatistics() {
		return Collections.singleton(memorystat);
	}
}
