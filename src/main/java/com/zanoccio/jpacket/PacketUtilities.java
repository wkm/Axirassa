
package com.zanoccio.jpacket;

public class PacketUtilities {

	/**
	 * Transforms an integer
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toByteArray(int value) {
		return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
	}


	/**
	 * Format an array of bytes in a hex dump format similar to WireShark.
	 */
	@SuppressWarnings("boxing")
	public static String toHexDump(byte[] array) {
		StringBuilder sb = new StringBuilder();

		for (int row = 0; row < array.length; row += 16) {
			if (row != 0)
				sb.append('\n');

			sb.append(String.format("%04X", row));
			sb.append(' ');

			// render hex
			for (int column = 0; column + row < array.length && column < 16; column++) {
				sb.append(' ');
				sb.append(String.format("%02x", array[column + row]));

				// extra break
				if (column == 7)
					sb.append(' ');
			}

			// TODO also render ASCII
		}

		return sb.toString();
	}
}
