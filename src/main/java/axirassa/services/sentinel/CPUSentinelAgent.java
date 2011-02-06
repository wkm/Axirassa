
package axirassa.services.sentinel;

import java.util.ArrayList;
import java.util.Collection;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarException;

import axirassa.services.sentinel.model.SentinelCPUStatisticEntity;

public class CPUSentinelAgent extends AbstractSentinelStatisticsAgent {

	private final ArrayList<SentinelCPUStatisticEntity> cpustats = new ArrayList<SentinelCPUStatisticEntity>();


	@Override
	public void execute() throws SigarException {
		cpustats.clear();

		CpuPerc[] cpus = getSigar().getCpuPercList();
		cpustats.ensureCapacity(cpus.length);

		int cpuid = 0;
		for (CpuPerc cpu : cpus) {
			SentinelCPUStatisticEntity datum = new SentinelCPUStatisticEntity();

			datum.machineid = getMachineID();
			datum.date = getDate();
			datum.cpu = cpuid;
			datum.system = cpu.getSys();
			datum.user = cpu.getUser();

			cpustats.add(datum);
			cpuid++;
		}
	}


	@Override
	public Collection<SentinelCPUStatisticEntity> getStatistics() {
		return cpustats;
	}
}
