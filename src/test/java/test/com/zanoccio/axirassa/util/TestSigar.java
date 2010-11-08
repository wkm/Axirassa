
package test.com.zanoccio.axirassa.util;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.junit.Test;

public class TestSigar {
	@Test
	public void cpuinfo() throws SigarException {
		// SigarLoader.require();

		Sigar sigar = new Sigar();
		Cpu cpu = sigar.getCpu();
		CpuInfo[] cpus = sigar.getCpuInfoList();
		for (CpuInfo info : cpus) {
			System.out.println(info);
		}

		for (CpuPerc perc : sigar.getCpuPercList()) {
			System.out.println(perc);
		}

		Mem mem = sigar.getMem();
		System.out.println("mem: " + mem);

		System.out.println(sigar.getNetInfo());

		NetInterfaceStat stat = sigar.getNetInterfaceStat("eth2");
		System.out.println("stat: " + stat);

	}
}
