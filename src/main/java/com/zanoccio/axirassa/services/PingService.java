
package com.zanoccio.axirassa.services;

import java.util.ArrayList;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.JRegistry;
import org.jnetpcap.packet.RegistryHeaderErrors;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Icmp.EchoRequest;

import com.zanoccio.axirassa.services.exceptions.CannotFindNetworkInterfacesException;
import com.zanoccio.axirassa.services.exceptions.PingServiceException;
import com.zanoccio.axirassa.util.PcapLoader;

public class PingService implements Service {

	private String host;


	public void setHost(String host) {
		this.host = host;
	}


	public String getHost() {
		return host;
	}


	@Override
	public void execute() throws PingServiceException, RegistryHeaderErrors {
		PcapLoader.require();

		List<PcapIf> devices = new ArrayList<PcapIf>();
		StringBuilder errorbuffer = new StringBuilder();

		int result = Pcap.findAllDevs(devices, errorbuffer);
		if (result == Pcap.NOT_OK || devices.isEmpty())
			throw new CannotFindNetworkInterfacesException(errorbuffer.toString());

		PcapIf device = devices.get(0);

		// open a network interface
		int snaplength = 64 * 1024;
		int flags = Pcap.MODE_PROMISCUOUS;
		int timeout = 10 * 1000;
		Pcap pcap = Pcap.openLive(device.getName(), snaplength, flags, timeout, errorbuffer);

		JRegistry.register(Icmp.class);
		JRegistry.register(EchoRequest.class);

		EchoRequest message = new EchoRequest();
		System.out.println("header: " + message.toHexdump());
		if (pcap.sendPacket(message.getHeader()) != Pcap.OK) {
			System.err.println(pcap.getErr());
		}

		pcap.close();
	}
}
