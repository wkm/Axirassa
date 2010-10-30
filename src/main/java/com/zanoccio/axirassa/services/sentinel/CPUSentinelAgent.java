
package com.zanoccio.axirassa.services.sentinel;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarException;

public class CPUSentinelAgent extends AbstractSentinelAgent {

	private final ArrayList<CPUStatistic> cpustats = new ArrayList<CPUStatistic>();


	@Override
	public void execute() throws SigarException {
		cpustats.clear();

		CpuPerc[] cpus = getSigar().getCpuPercList();
		cpustats.ensureCapacity(cpus.length);

		int cpuid = 0;
		for (CpuPerc cpu : cpus) {
			cpustats.add(new CPUStatistic(getMachineID(), getDate(), cpuid, cpu.getSys(), cpu.getUser()));
			cpuid++;
		}
	}


	@Override
	public void save(Session session) {
		System.out.println("Saving CPU stats");
		for (CPUStatistic stat : cpustats)
			stat.save(session);
	}

}
