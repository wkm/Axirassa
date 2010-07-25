
package com.zanoccio.jpacket;

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

	private final byte[] address;


	/**
	 * Parse a MAC address in the form 00:00:00:00:00:00. Gives null if the
	 * address cannot be parsed.
	 * 
	 * @param address
	 * @return
	 */
	public static MACAddress parse(String address) {
		String[] components = address.split(":");

		if (components.length != 6)
			return null;

		byte[] values = new byte[6];
		for (int i = 0; i < 6; i++)
			values[i] = Byte.parseByte(components[i], 16);

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


	@Override
	public int size() {
		return 6;
	}


	@Override
	public byte[] getBytes() {
		return address;
	}

}
