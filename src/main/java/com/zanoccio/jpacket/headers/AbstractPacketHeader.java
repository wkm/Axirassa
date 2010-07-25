
package com.zanoccio.jpacket.headers;

import java.util.List;

public abstract class AbstractPacketHeader implements PacketHeader {

	@Override
	public List<Byte> construct() {
		return null;
	}
}
