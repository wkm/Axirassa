
package packetkit.headers;

import packetkit.ByteTrie;
import packetkit.PacketFragment;
import packetkit.PacketUtilities;
import packetkit.headers.annotations.FixedSize;

@FixedSize(size = 2)
public enum ARPOpcode implements PacketFragment {
	REQUEST(0x0001),
	REPLY(0x0002);

	private static final ByteTrie<ARPOpcode> TRIE = PacketUtilities.trieFromEnum(ARPOpcode.class);


	public static ARPOpcode fromBytes(byte[] buffer, int slot, int length) {
		return TRIE.get(buffer, slot, length);
	}


	// there are many more:
	// http://www.iana.org/assignments/arp-parameters/arp-parameters.xml

	private byte[] bytes;


	ARPOpcode(int code) {
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
