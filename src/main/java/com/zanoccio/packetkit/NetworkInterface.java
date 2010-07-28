
package com.zanoccio.packetkit;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.jnetpcap.ByteBufferHandler;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.PcapIf;
import org.jnetpcap.PcapSockAddr;

import com.zanoccio.packetkit.exceptions.NoIP4AddressException;
import com.zanoccio.packetkit.exceptions.PacketKitException;
import com.zanoccio.packetkit.frames.Frame;

public class NetworkInterface {

	private final int snaplength = 1024;
	private final int flags = Pcap.MODE_NON_BLOCKING;
	private final int timeout = 1000;
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


	public NetworkInterface(PcapIf device) throws PacketKitException, IOException {
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


	public void sendPacket(Frame frame) throws PacketKitException {
		frame.associate(this);
		pcap.sendPacket(frame.construct());
	}


	public void liveCapture() {
		ByteBufferHandler<String> handler = new ByteBufferHandler<String>() {
			@Override
			public void nextPacket(PcapHeader header, ByteBuffer buffer, String user) {
				byte[] bytes = new byte[buffer.capacity()];
				buffer.get(bytes);

				System.out.println("\n\n\n\n\n======================");
				System.out.println("user: " + user);
				System.out.println("header: " + header.size());
				System.out.println("buffer size: " + buffer.capacity());
				System.out.println(PacketUtilities.toHexDump(bytes));
			}
		};

		pcap.loop(1, handler, "");
	}
}
