
package com.zanoccio.packetkit.headers;

import com.zanoccio.packetkit.ByteTrie;
import com.zanoccio.packetkit.PacketFragment;
import com.zanoccio.packetkit.PacketUtilities;
import com.zanoccio.packetkit.headers.annotations.FixedSize;

@FixedSize(size = 2)
public enum EtherType implements PacketFragment {
	IP4(0x0800),
	IP6(0x86DD),
	SYN3(0x1337),
	ARP(0x0806);

	private static final ByteTrie<EtherType> TRIE = PacketUtilities.trieFromEnum(EtherType.class);


	public static EtherType fromBytes(byte[] buffer, int slot, int length) {
		return TRIE.get(buffer, slot, length);
	}


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
