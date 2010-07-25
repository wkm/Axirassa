
package com.zanoccio.jpacket;

import com.zanoccio.jpacket.exceptions.JPacketException;
import com.zanoccio.jpacket.exceptions.MalformedIP4AddressException;

public class IP4Address implements PacketFragment {

	/**
	 * Contains the address 255.255.255.255
	 */
	public static final IP4Address BROADCAST = IP4Address.parse("255.255.255.255");

	/**
	 * Contains the address 127.0.0.1
	 */
	public static final IP4Address LOCALHOST = IP4Address.parse("127.0.0.1");

	/**
	 * Contains the address 0.0.0.0
	 */
	public static final IP4Address EMPTY = IP4Address.parse("0.0.0.0");


	/**
	 * Parses an {@link IP4Address} in the form <tt>00.00.00.00</tt>.
	 * 
	 * Gives null if the address could not be parsed.
	 */
	public static IP4Address parse(String address) {
		String[] components = address.split(".");
		if (components.length != 4)
			return null;

		byte[] bytes = new byte[4];
		for (int i = 0; i < 4; i++)
			bytes[i] = Byte.parseByte(components[i]);

		try {
			return new IP4Address(bytes);
		} catch (JPacketException e) {
			return null;
		}
	}


	private final byte[] address;


	public IP4Address(byte[] address) throws JPacketException {
		if (address.length != 4)
			throw new MalformedIP4AddressException(address);

		this.address = address;
	}


	public IP4Address(byte a, byte b, byte c, byte d) {
		address = new byte[] { a, b, c, d };
	}


	@Override
	public byte[] getBytes() {
		return address;
	}


	@Override
	public int size() {
		return 4;
	}

}
