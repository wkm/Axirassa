
package com.zanoccio.axirassa.services;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.RegistryHeaderErrors;

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
	public void execute() throws PingServiceException, RegistryHeaderErrors, IOException {
		PcapLoader.require();

		List<PcapIf> devices = new ArrayList<PcapIf>();
		StringBuilder errorbuffer = new StringBuilder();

		int result = Pcap.findAllDevs(devices, errorbuffer);
		if (result == Pcap.NOT_OK || devices.isEmpty())
			throw new CannotFindNetworkInterfacesException(errorbuffer.toString());

		PcapIf device = devices.get(1);
		device.getAddresses();
		System.out.println(device.getName());
		System.out.println(device.getAddresses());
		for (byte b : device.getHardwareAddress())
			System.out.print(Integer.toHexString(b) + " ");

		// open a network interface
		int snaplength = 64 * 1024;
		int flags = Pcap.MODE_NON_BLOCKING;
		int timeout = 10 * 1000;
		Pcap pcap = Pcap.openLive(device.getName(), snaplength, flags, timeout, errorbuffer);

		byte[] msg = new byte[14];
		Arrays.fill(msg, (byte) 0xff);
		ByteBuffer msgbuff = ByteBuffer.wrap(msg);

		if (pcap.sendPacket(msgbuff) != Pcap.OK)
			System.err.println(pcap.getErr());

		pcap.close();
	}
}
