
package test.com.zanoccio.axirassa.util;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.junit.Test;

import com.zanoccio.axirassa.util.SigarLoader;

public class TestSigar {
	@Test
	public void cpuinfo() throws SigarException {
		SigarLoader.require();

		Sigar sigar = new Sigar();
		Cpu cpu = sigar.getCpu();
		CpuInfo[] cpus = sigar.getCpuInfoList();

		for (CpuInfo info : cpus) {
			System.out.println("Model: " + info.getModel());
			System.out.println("Mhz: " + info.getMhz());
		}

	}
}
