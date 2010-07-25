
package com.zanoccio.jpacket.headers;

import com.zanoccio.jpacket.PacketFragment;
import com.zanoccio.jpacket.PacketUtilities;

public enum ARPHardwareType implements PacketFragment {
	ETHERNET(0x0001);

	private byte[] bytes;


	ARPHardwareType(int code) {
		bytes = PacketUtilities.toByteArray(code);
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
