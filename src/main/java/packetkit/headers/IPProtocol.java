
package packetkit.headers;

import packetkit.ByteTrie;
import packetkit.PacketFragment;
import packetkit.PacketUtilities;
import packetkit.headers.annotations.FixedSize;

@FixedSize(size = 1)
public enum IPProtocol implements PacketFragment {
	ICMP(0x01);

	private static final ByteTrie<IPProtocol> TRIE = PacketUtilities.trieFromEnum(IPProtocol.class);


	public static IPProtocol fromBytes(byte[] buffer, int slot, int length) {
		return TRIE.get(buffer, slot, length);
	}


	private byte[] bytes;


	IPProtocol(int code) {
		bytes = new byte[] { (byte) code };
	}


	@Override
	public byte[] getBytes() {
		return bytes;
	}


	@Override
	public int size() {
		return 1;
	}
}
