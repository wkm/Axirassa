
package com.zanoccio.jpacket;

import java.util.Arrays;

import com.zanoccio.jpacket.exceptions.JPacketException;
import com.zanoccio.jpacket.exceptions.MalformedMACAddressException;

/**
 * Represents a media access control address.
 * 
 * Since {@link MACAddress} implements {@link PacketFragment} it can be directly
 * included in a packet header.
 * 
 * @author wiktor
 */
public class MACAddress implements PacketFragment {

	public static final MACAddress EMPTY = MACAddress.parse("0:0:0:0:0:0");
	public static final MACAddress BROADCAST = MACAddress.parse("ff:ff:ff:ff:ff:ff");

	private final byte[] address;


	/**
	 * Parse a MAC address in the form 00:00:00:00:00:00. Gives null if the
	 * address cannot be parsed.
	 * 
	 * @param address
	 * @return
	 */
	public static MACAddress parse(String address) {
		String[] components = address.split(":", 6);

		if (components.length != 6)
			return null;

		byte[] values = new byte[6];
		for (int i = 0; i < 6; i++)
			try {
				int integer = Integer.parseInt(components[i], 16);
				if (integer < 0 || 255 < integer)
					return null;

				values[i] = (byte) integer;
			} catch (NumberFormatException e) {
				return null;
			}

		try {
			return new MACAddress(values);
		} catch (JPacketException e) {
			return null;
		}
	}


	/**
	 * Creates a {@link MACAddress} from an array of six bytes.
	 * 
	 * @param values
	 * @throws JPacketException
	 *             if the array does not contain six bytes.
	 */
	public MACAddress(byte[] values) throws JPacketException {
		if (values.length != 6)
			throw new MalformedMACAddressException(values);

		address = values;
	}


	/**
	 * Creates a {@link MACAddress} from six individual bytes.
	 */
	public MACAddress(byte a, byte b, byte c, byte d, byte e, byte f) {
		address = new byte[] { a, b, c, d, e, f };
	}


	//
	// PacketFragment
	//
	@Override
	public int size() {
		return 6;
	}


	@Override
	public byte[] getBytes() {
		return address;
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

		if (other instanceof MACAddress) {
			MACAddress o = (MACAddress) other;
			return Arrays.equals(address, o.address);
		}

		return false;
	}


	@SuppressWarnings("boxing")
	@Override
	public String toString() {
		return String.format("%02x:%02x:%02x:%02x:%02x:%02x", address[0], address[1], address[2], address[3],
		                     address[4], address[5]);
	}

}
