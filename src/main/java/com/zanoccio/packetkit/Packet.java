
package com.zanoccio.packetkit;

import java.util.ArrayList;
import java.util.List;

import com.zanoccio.packetkit.exceptions.JPacketException;
import com.zanoccio.packetkit.headers.PacketHeader;

public class Packet {

	private final List<PacketHeader> headers;
	private ArrayList<Byte> body;


	public Packet() {
		headers = new ArrayList<PacketHeader>();
	}


	public List<Byte> construct() throws JPacketException {
		if (body != null)
			return body;

		body = new ArrayList<Byte>();

		for (PacketHeader header : headers)
			body.addAll(header.construct());

		return body;
	}


	/**
	 * @return
	 * @throws JPacketException
	 */
	public byte[] constructBytes() throws JPacketException {
		construct();

		byte[] bytes = new byte[body.size()];

		int i = 0;
		for (Byte curbyte : body)
			bytes[i++] = curbyte.byteValue();

		return bytes;
	}
}
