
package com.zanoccio.axirassa.services.sentinel;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.SigarException;

public class NetworkSentinelAgent extends AbstractSentinelStatisticsAgent {

	private final ArrayList<NetworkStatistic> networkstat = new ArrayList<NetworkStatistic>();


	@Override
	public void execute() throws SigarException {
		networkstat.clear();

		String[] netinterfaces = getSigar().getNetInterfaceList();
		networkstat.ensureCapacity(netinterfaces.length);
		for (String netinterface : netinterfaces) {
			NetInterfaceStat stat = getSigar().getNetInterfaceStat(netinterface);
			String interfacename = getSigar().getNetInterfaceConfig(netinterface).getDescription();
			networkstat.add(new NetworkStatistic(getMachineID(), getDate(), interfacename, stat.getTxBytes(), stat
			        .getRxBytes()));
		}

	}


	@Override
	public void save(Session session) {
		for (NetworkStatistic stat : networkstat)
			stat.save(session);
	}

}
