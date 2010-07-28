
package com.zanoccio.packetkit.headers;

import com.zanoccio.packetkit.PacketFragment;
import com.zanoccio.packetkit.PacketUtilities;

public enum EtherType implements PacketFragment {
	IP4(0x0800),
	IP6(0x86DD),
	SYN3(0x1337),
	ARP(0x0806);

	private byte[] bytes;


	EtherType(int code) {
		bytes = PacketUtilities.toByteArray((short) code);
	}


	@Override
	public byte[] getBytes() {
		return bytes;
	}


	@Override
	public int size() {
		return 2;
	}
}
