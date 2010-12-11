
package packetkit.headers;

import packetkit.ByteTrie;
import packetkit.PacketFragment;
import packetkit.PacketUtilities;
import packetkit.headers.annotations.FixedSize;

@FixedSize(size = 2)
public enum ARPHardwareType implements PacketFragment {
	ETHERNET(0x01),
	IEEE_802_NETWORKS(0x06);

	private static final ByteTrie<ARPHardwareType> TRIE = PacketUtilities.trieFromEnum(ARPHardwareType.class);


	public static ARPHardwareType fromBytes(byte[] buffer, int slot, int length) {
		return TRIE.get(buffer, slot, length);
	}


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
