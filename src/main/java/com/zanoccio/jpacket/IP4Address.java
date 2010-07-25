
package com.zanoccio.jpacket;

import java.util.Arrays;

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
		String[] components = address.split("\\.", 4);
		if (components.length != 4)
			return null;

		byte[] bytes = new byte[4];
		for (int i = 0; i < 4; i++)
			try {
				int num = Integer.parseInt(components[i]);

				if (num < 0 || 255 < num)
					return null;

				bytes[i] = (byte) num;
			} catch (NumberFormatException e) {
				return null;
			}

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


	//
	// PacketFragment
	//

	@Override
	public byte[] getBytes() {
		return address;
	}


	@Override
	public int size() {
		return 4;
	}


	//
	// Object
	//

	@Override
	public int hashCode() {
		return Arrays.hashCode(address);
	}


	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;

		if (other instanceof IP4Address) {
			IP4Address o = (IP4Address) other;
			return Arrays.equals(address, o.address);
		}

		return false;
	}


	@SuppressWarnings("boxing")
	@Override
	public String toString() {
		return String.format("%d.%d.%d.%d", address[0] & 0x000000FF, address[1] & 0x000000FF, address[2] & 0x000000FF,
		                     address[3] & 0x000000FF);
	}

}
