
package com.zanoccio.packetkit;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
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


	// by tschodt from http://forums.sun.com/thread.jspa?threadID=628082
	public static int intFromByteArray(byte[] b) {
		return b[0] << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8 | (b[3] & 0xff);
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


	public static byte[] parseHexDump(String dump) {

		String[] components = dump.split("[ \t\n]+");
		ArrayList<Byte> bytes = new ArrayList<Byte>();

		for (String component : components)
			if (component.length() == 2) {
				try {
					bytes.add(Integer.valueOf(component, 16).byteValue());
				} catch (NumberFormatException e) {
					System.out.println("could not parse: " + component);
				}
			}

		byte[] array = new byte[bytes.size()];
		int i = 0;
		for (Byte b : bytes)
			array[i++] = b.byteValue();

		return array;
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
