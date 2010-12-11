
package packetkit;

import java.io.IOException;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.PcapIf;
import org.jnetpcap.PcapSockAddr;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.nio.JMemory;

import packetkit.exceptions.NoIP4AddressException;
import packetkit.exceptions.PacketKitException;
import packetkit.frames.Frame;


public class NetworkInterface {

	private final int snaplength = 1024;
	private final int flags = Pcap.MODE_NON_BLOCKING;
	private final int timeout = 1;
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
		byte[] bytes = frame.construct();
		System.out.println("cons: " + System.currentTimeMillis());
		pcap.sendPacket(bytes);
	}


	public void liveCapture() {
		PcapHeader header = new PcapHeader();
		JBuffer buffer = new JBuffer(JMemory.Type.POINTER);

		pcap.nextEx(header, buffer);
		System.out.println("======================");
		System.out.println(PacketUtilities.toHexDump(buffer.getByteArray(0, buffer.size())));
		System.out.println("Now: " + System.currentTimeMillis());
		System.out.println("Cap: " + header.timestampInMillis());

		pcap.close();
	}
}
