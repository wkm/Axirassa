
package com.zanoccio.jpacket;

import java.io.IOException;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapIf;
import org.jnetpcap.PcapSockAddr;

import com.zanoccio.jpacket.exceptions.JPacketException;
import com.zanoccio.jpacket.exceptions.NoIP4AddressException;

public class NetworkInterface {

	private final int snaplength = 1024;
	private final int flags = Pcap.MODE_NON_BLOCKING;
	private final int timeout = 10 * 1000;
	private final StringBuilder errorbuffer;

	private final PcapIf device;

	protected IP4Address ipaddress;
	protected MACAddress macaddress;

	private Pcap pcap;


	protected NetworkInterface() {
		errorbuffer = null;
		device = null;

		ipaddress = null;
		macaddress = null;

		pcap = null;
	}


	public NetworkInterface(PcapIf device) throws JPacketException, IOException {
		this.device = device;
		this.macaddress = new MACAddress(device.getHardwareAddress());

		errorbuffer = new StringBuilder();

		// find the IP4 address for this network interface
		IP4Address ipaddress = null;
		for (PcapAddr address : device.getAddresses()) {
			if (address.getAddr().getFamily() == PcapSockAddr.AF_INET) {
				ipaddress = new IP4Address(address.getAddr().getData());
				break;
			}
		}

		if (ipaddress == null)
			throw new NoIP4AddressException(device);

		this.ipaddress = ipaddress;
	}


	public void openLive() {
		pcap = Pcap.openLive(device.getName(), snaplength, flags, timeout, errorbuffer);
	}


	public MACAddress getMACAddress() {
		return macaddress;
	}


	public IP4Address getIP4Address() {
		return ipaddress;
	}
}
