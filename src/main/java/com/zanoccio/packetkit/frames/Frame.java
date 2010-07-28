
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
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		for (PacketHeader header : headers) {
			header.associate(networkinterface);
			bytes.addAll(header.construct());
		}

		byte[] array = new byte[bytes.size()];
		int i = 0;
		for (Byte b : bytes)
			array[i++] = b.byteValue();

		return array;
	}


	public void associate(NetworkInterface networkinterface) {
		this.networkinterface = networkinterface;
	}
}
