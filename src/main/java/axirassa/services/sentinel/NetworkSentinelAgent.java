
package axirassa.services.sentinel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.SigarException;

import axirassa.services.sentinel.model.SentinelNetworkIOStatisticEntity;
import axirassa.services.sentinel.model.SentinelNetworkStatisticEntity;
import axirassa.services.sentinel.model.SentinelStatisticEntity;

public class NetworkSentinelAgent extends AbstractSentinelStatisticsAgent {

	private final ArrayList<SentinelStatisticEntity> networkstats = new ArrayList<SentinelStatisticEntity>();
	private final HashMap<String, NetworkIOSnapshot> previoussnapshots = new HashMap<String, NetworkIOSnapshot>();


	@Override
	public void execute() throws SigarException {
		networkstats.clear();

		String[] netinterfaces = getSigar().getNetInterfaceList();
		networkstats.ensureCapacity(netinterfaces.length);
		for (String netinterface : netinterfaces) {
			NetInterfaceStat stat = null;
			try {
				stat = getSigar().getNetInterfaceStat(netinterface);
			} catch (SigarException e) {
				// ignore, keep going
			}

			if (stat == null)
				continue;

			String interfacename = getSigar().getNetInterfaceConfig(netinterface).getDescription();

			NetworkIOSnapshot current = new NetworkIOSnapshot();
			current.date = getDate();
			current.rxbytes = stat.getRxBytes();
			current.txbytes = stat.getTxBytes();

			SentinelNetworkStatisticEntity netdatum = new SentinelNetworkStatisticEntity();

			netdatum.machineid = getMachineID();
			netdatum.date = getDate();
			netdatum.device = interfacename;
			netdatum.send = current.txbytes;
			netdatum.receive = current.rxbytes;

			networkstats.add(netdatum);

			NetworkIOSnapshot previous = previoussnapshots.get(interfacename);
			if (previous != null) {
				long millis = getDate().getTime() - previous.date.getTime();
				long seconds = millis / 1000;

				long rxrate = (current.rxbytes - previous.rxbytes) / seconds;
				long txrate = (current.txbytes - previous.txbytes) / seconds;

				SentinelNetworkIOStatisticEntity netiodatum = new SentinelNetworkIOStatisticEntity();

				netiodatum.machineid = getMachineID();
				netiodatum.date = getDate();
				netiodatum.device = interfacename;
				netiodatum.receive = rxrate;
				netiodatum.send = txrate;

				networkstats.add(netiodatum);
			}

			previoussnapshots.put(interfacename, current);
		}

	}


	@Override
	public Collection<SentinelStatisticEntity> getStatistics() {
		return networkstats;
	}

}

class NetworkIOSnapshot {
	Date date;
	long rxbytes;
	long txbytes;
}
