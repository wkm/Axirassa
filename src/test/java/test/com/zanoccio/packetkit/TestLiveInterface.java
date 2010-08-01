
package test.com.zanoccio.packetkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.junit.Test;

import com.zanoccio.axirassa.services.exceptions.CannotFindNetworkInterfacesException;
import com.zanoccio.axirassa.services.exceptions.PingServiceException;
import com.zanoccio.axirassa.util.PcapLoader;
import com.zanoccio.packetkit.IP4Address;
import com.zanoccio.packetkit.NetworkInterface;
import com.zanoccio.packetkit.exceptions.PacketKitException;
import com.zanoccio.packetkit.frames.Frame;
import com.zanoccio.packetkit.headers.ARPHeader;
import com.zanoccio.packetkit.headers.EtherType;
import com.zanoccio.packetkit.headers.MACHeader;

public class TestLiveInterface {

	@Test
	public void liveInterface() throws PacketKitException, IOException, PingServiceException {
		PcapLoader.require();

		List<PcapIf> devices = new ArrayList<PcapIf>();
		StringBuilder errorbuffer = new StringBuilder();

		int result = Pcap.findAllDevs(devices, errorbuffer);
		if (result == Pcap.NOT_OK || devices.isEmpty())
			throw new CannotFindNetworkInterfacesException(errorbuffer.toString());

		NetworkInterface netinterface = new NetworkInterface(devices.get(1));

		Frame whohas = new Frame();

		MACHeader macheader = MACHeader.broadcast();
		macheader.setType(EtherType.ARP);
		whohas.addHeader(macheader);

		ARPHeader arpheader = ARPHeader.whoHas(IP4Address.parse("192.168.1.8"));
		whohas.addHeader(arpheader);

		netinterface.openLive();
		for (int i = 0; i < 1; i++) {
			netinterface.sendPacket(whohas);
		}

		// netinterface.liveCapture();
	}
}
