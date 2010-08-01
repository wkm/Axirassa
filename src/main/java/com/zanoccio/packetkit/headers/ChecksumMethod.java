
package com.zanoccio.packetkit.headers;

import com.zanoccio.packetkit.PacketUtilities;

/**
 * Enumeration defining various commonly used checksum methods.
 * 
 * @author wiktor
 */
public enum ChecksumMethod {
	/**
	 * Compute the 16-bit one's complement checksum
	 */
	ONESCOMPLEMENT {
		@Override
		public byte[] compute(byte[] array) {
			int total = 0;
			for (int i = 0; i < array.length; i += 2) {
				total += 0x0000ffff & (array[i] << 8 | (array[i + 1] & 0xff));
			}

			int withcarries = total + (total >> 16);

			return PacketUtilities.toByteArray((short) ~withcarries);
		}
	};

	abstract public byte[] compute(byte[] array);
}
