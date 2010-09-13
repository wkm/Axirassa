
package com.zanoccio.packetkit.frames;

import java.util.ArrayList;
import java.util.List;

import com.zanoccio.packetkit.ByteParser;
import com.zanoccio.packetkit.NetworkInterface;
import com.zanoccio.packetkit.exceptions.PacketKitException;
import com.zanoccio.packetkit.headers.PacketHeader;

public class Frame {
	private List<PacketHeader> headers;
	private NetworkInterface networkinterface;


	public Frame() {
		headers = new ArrayList<PacketHeader>();
	}


	public void addHeader(PacketHeader header) {
		headers.add(header);
	}


	public List<PacketHeader> getHeaders() {
		return headers;
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


	public boolean deconstruct(List<PacketHeader> headers, byte[] buffer) {
		return deconstruct(headers, buffer, 0, buffer.length);
	}


	public boolean deconstruct(List<PacketHeader> headers, byte[] buffer, int start, int length) {
		ByteParser container = new ByteParser(buffer, start);
		this.headers = headers;

		for (PacketHeader header : headers) {
			try {
				header.deconstruct(container, length);
			} catch (PacketKitException e) {
				return false;
			}
		}

		return true;
	}


	public void associate(NetworkInterface networkinterface) {
		this.networkinterface = networkinterface;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (PacketHeader header : headers) {
			sb.append(header);
			sb.append('\n');
		}

		return sb.toString();
	}
}
