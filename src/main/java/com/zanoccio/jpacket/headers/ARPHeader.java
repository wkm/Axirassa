
package com.zanoccio.jpacket.headers;

import org.bouncycastle.util.IPAddress;

import com.zanoccio.jpacket.MACAddress;
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
	private MACAddress sendermac;

	@StaticFragment(slot = 6)
	private IPAddress senderip;

	@StaticFragment(slot = 7)
	private MACAddress targetmac;

	@StaticFragment(slot = 8)
	private IPAddress targetip;
}
