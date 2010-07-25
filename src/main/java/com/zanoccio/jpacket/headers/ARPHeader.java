
package com.zanoccio.jpacket.headers;

import org.bouncycastle.util.IPAddress;

import com.zanoccio.jpacket.MACAddress;
import com.zanoccio.jpacket.headers.annotations.FromNetworkInterface;
import com.zanoccio.jpacket.headers.annotations.StaticFragment;

public class ARPHeader extends AbstractPacketHeader {

	@StaticFragment(slot = 0, size = 4)
	private ARPHardwareType hardwaretype;

	@StaticFragment(slot = 1, size = 4)
	private ARPProtocolType protocoltype;

	@StaticFragment(slot = 2, size = 2)
	private int hardwaresize;

	@StaticFragment(slot = 3, size = 2)
	private int protocolsize;

	@StaticFragment(slot = 4, size = 4)
	private ARPOpcode opcode;

	@StaticFragment(slot = 5)
	@FromNetworkInterface
	private MACAddress sendermac;

	@StaticFragment(slot = 6)
	@FromNetworkInterface
	private IPAddress senderip;

	@StaticFragment(slot = 7)
	private MACAddress targetmac;

	@StaticFragment(slot = 8)
	private IPAddress targetip;


	/**
	 * Constructs a whoHas ARP packet broadcast on the local network
	 * 
	 * @param addr
	 * @return
	 */
	public static ARPHeader whoHas(IPAddress addr) {
		ARPHeader header = new ARPHeader();
		header.hardwaretype = ARPHardwareType.ETHERNET;
		header.protocoltype = ARPProtocolType.IP4;
		header.hardwaresize = 6;
		header.protocolsize = 4;

		header.targetmac = MACAddress.EMPTY;
		header.targetip = addr;

		return header;
	}


	public ARPHeader() {
		hardwaretype = ARPHardwareType.ETHERNET;
		protocoltype = ARPProtocolType.IP4;
		hardwaresize = 6; // for ETHERNET
		protocolsize = 4; // for IP
	}

	// ACCESSORS
}
