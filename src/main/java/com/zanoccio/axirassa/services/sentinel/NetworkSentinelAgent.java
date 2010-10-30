
package com.zanoccio.axirassa.services.sentinel;

import java.util.ArrayList;
import java.util.Collection;

import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.SigarException;

public class NetworkSentinelAgent extends AbstractSentinelStatisticsAgent {

	private final ArrayList<NetworkStatistic> networkstats = new ArrayList<NetworkStatistic>();


	@Override
	public void execute() throws SigarException {
		networkstats.clear();

		String[] netinterfaces = getSigar().getNetInterfaceList();
		networkstats.ensureCapacity(netinterfaces.length);
		for (String netinterface : netinterfaces) {
			NetInterfaceStat stat = getSigar().getNetInterfaceStat(netinterface);
			String interfacename = getSigar().getNetInterfaceConfig(netinterface).getDescription();
			networkstats.add(new NetworkStatistic(getMachineID(), getDate(), interfacename, stat.getTxBytes(), stat
			        .getRxBytes()));
		}

	}


	@Override
	public Collection<NetworkStatistic> getStatistics() {
		return networkstats;
	}

}
