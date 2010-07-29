
package com.zanoccio.packetkit.frames;

import java.util.ArrayList;
import java.util.List;

import com.zanoccio.packetkit.NetworkInterface;
import com.zanoccio.packetkit.exceptions.PacketKitException;
import com.zanoccio.packetkit.headers.PacketHeader;

public class Frame {
	private final List<PacketHeader> headers;
	private NetworkInterface networkinterface;


	public Frame() {
		headers = new ArrayList<PacketHeader>();
	}


	public void addHeader(PacketHeader header) {
		headers.add(header);
	}


	public byte[] construct() throws PacketKitException {
		byte[][] fragments = new byte[headers.size()][];

		// if (body != null)
		// return body;

		// construct each component
		int i = 0;
		int totallength = 0;
		for (PacketHeader header : headers) {
			header.associate(networkinterface);
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
		// this.body = body;

		return body;
	}


	public void associate(NetworkInterface networkinterface) {
		this.networkinterface = networkinterface;
	}
}
