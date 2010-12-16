
package axirassa.services.sentinel;

import java.util.ArrayList;
import java.util.Collection;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarException;

import axirassa.services.sentinel.model.SentinelCPUStatisticModel;

public class CPUSentinelAgent extends AbstractSentinelStatisticsAgent {

	private final ArrayList<SentinelCPUStatisticModel> cpustats = new ArrayList<SentinelCPUStatisticModel>();


	@Override
	public void execute() throws SigarException {
		cpustats.clear();

		CpuPerc[] cpus = getSigar().getCpuPercList();
		cpustats.ensureCapacity(cpus.length);

		int cpuid = 0;
		for (CpuPerc cpu : cpus) {
			SentinelCPUStatisticModel datum = new SentinelCPUStatisticModel();

			datum.machineid = getMachineID();
			datum.date = getDate();
			datum.cpuid = cpuid;
			datum.system = cpu.getSys();
			datum.user = cpu.getUser();

			cpustats.add(datum);
			cpuid++;
		}
	}


	@Override
	public Collection<SentinelCPUStatisticModel> getStatistics() {
		return cpustats;
	}
}
