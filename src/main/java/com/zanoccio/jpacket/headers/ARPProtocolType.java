
package com.zanoccio.jpacket.headers;

import com.zanoccio.jpacket.PacketFragment;
import com.zanoccio.jpacket.PacketUtilities;

public enum ARPProtocolType implements PacketFragment {
	IP4(0x0800);

	private byte[] bytes;


	ARPProtocolType(int code) {
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
