
package com.zanoccio.packetkit;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.zanoccio.packetkit.exceptions.PacketKitException;
import com.zanoccio.packetkit.frames.Frame;
import com.zanoccio.packetkit.headers.PacketHeader;

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


	public static byte[] toByteArray(short value) {
		return new byte[] { (byte) (value >>> 8), (byte) value };
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


	public static String toHexDumpFragment(byte[] bytes) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < bytes.length; i++) {
			if (i > 0)
				sb.append(' ');

			sb.append(String.format("%02x", bytes[i]));
		}

		return sb.toString();
	}


	public static void assertPacketEquals(String hexdump, PacketHeader packet) throws PacketKitException {
		assertPacketEquals(hexdump, packet.construct());
	}


	public static void assertPacketEquals(String hexdump, Frame frame) throws PacketKitException {
		assertPacketEquals(hexdump, frame.construct());
	}


	@SuppressWarnings("boxing")
	public static void assertPacketEquals(String hexdump, List<Byte> list) {
		byte[] bytes = new byte[list.size()];
		for (int i = 0; i < list.size(); i++)
			bytes[i] = list.get(i);

		assertPacketEquals(hexdump, bytes);
	}


	public static void assertPacketEquals(String hexdump, byte[] bytes) {
		assertEquals(hexdump, toHexDump(bytes));
	}
}
