
package packetkit;

import java.util.ArrayList;
import java.util.List;

import packetkit.exceptions.PacketKitException;
import packetkit.headers.PacketHeader;


public class Packet {

	private final List<PacketHeader> headers;
	private byte[] body;


	public Packet() {
		headers = new ArrayList<PacketHeader>();
		body = null;
	}


	public byte[] construct() throws PacketKitException {
		byte[][] fragments = new byte[headers.size()][];

		if (body != null)
			return body;

		// construct each component
		int i = 0;
		int totallength = 0;
		for (PacketHeader header : headers) {
			fragments[i] = header.construct();
			totallength += fragments[i].length;

			i++;
		}

		// shove the fragments together
		byte[] body = new byte[totallength];
		int index = 0;
		for (byte[] fragment : fragments)
			for (byte b : fragment)
				body[index++] = b;

		// cache
		this.body = body;

		return body;
	}
}
