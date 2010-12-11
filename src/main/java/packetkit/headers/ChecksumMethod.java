
package packetkit.headers;

import packetkit.PacketUtilities;

/**
 * Enumeration defining various commonly used checksum methods.
 * 
 * @author wiktor
 */
public enum ChecksumMethod {
	/**
	 * Compute the 16-bit one's complement checksum.
	 * 
	 * (implemented by following along http://www.netfor2.com/checksum.html)
	 */
	ONESCOMPLEMENT {
		@Override
		public byte[] compute(byte[] array, int offset, int length) {
			int total = 0;
			for (int i = offset; i < offset + length; i += 2) {
				total += 0x0000ffff & (array[i] << 8 | (array[i + 1] & 0xff));
			}

			int withcarries = total + (total >> 16);

			return PacketUtilities.toByteArray((short) ~withcarries);
		}
	};

	abstract public byte[] compute(byte[] array, int offset, int length);


	public byte[] compute(byte[] array) {
		return compute(array, 0, array.length);
	}
}
