
package com.zanoccio.packetkit.headers;

import com.zanoccio.packetkit.ByteTrie;
import com.zanoccio.packetkit.PacketFragment;
import com.zanoccio.packetkit.PacketUtilities;
import com.zanoccio.packetkit.headers.annotations.FixedSize;

@FixedSize(size = 2)
public enum ARPProtocolType implements PacketFragment {
	IP4(0x0800);

	private static final ByteTrie<ARPProtocolType> TRIE = PacketUtilities.trieFromEnum(ARPProtocolType.class);


	public static ARPProtocolType fromBytes(byte[] buffer, int slot, int length) {
		return TRIE.get(buffer, slot, length);
	}


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
