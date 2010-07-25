
package com.zanoccio.jpacket.headers;

import com.zanoccio.jpacket.FragmentOpcode;
import com.zanoccio.jpacket.PacketUtilities;

public enum ARPHardwareType implements FragmentOpcode {
	ETHERNET(0x0001);

	private byte[] bytes;


	ARPHardwareType(int code) {
		bytes = PacketUtilities.toByteArray(code);
	}


	@Override
	public byte[] getBytes() {
		return bytes;
	}
}
