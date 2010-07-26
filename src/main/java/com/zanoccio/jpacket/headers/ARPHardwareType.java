
package com.zanoccio.jpacket.headers;

import com.zanoccio.jpacket.PacketFragment;
import com.zanoccio.jpacket.PacketUtilities;

public enum ARPHardwareType implements PacketFragment {
	ETHERNET(0x01);

	private byte[] bytes;


	ARPHardwareType(int code) {
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
