
package packetkit;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import packetkit.exceptions.InvalidFieldError;
import packetkit.exceptions.PacketKitException;
import packetkit.frames.Frame;
import packetkit.headers.PacketHeader;


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
		return intFromByteArray(b, 0, 4);
	}


	public static int intFromByteArray(byte[] b, int start) {
		return intFromByteArray(b, start, 4);
	}


	public static int intFromByteArray(byte[] b, int start, int length) {
		switch (length) {
		case 0:
			return 0;

		case 1:
			return b[start] & 0xff;

		case 2:
			return (b[start] & 0xff) << 8 | (b[start + 1] & 0xff);

		case 3:
			return (b[start] & 0xff) << 16 | (b[start + 1] & 0xff) << 8 | (b[start + 2] & 0xff);

		case 4:
		default:
			return b[start] << 24 | (b[start + 1] & 0xff) << 16 | (b[start + 2] & 0xff) << 8 | (b[start + 3] & 0xff);
		}
	}


	public static short shortFromByteArray(byte[] b) {
		return (short) (b[0] << 8 | (b[1] & 0xff));
	}


	public static short shortFromByteArray(byte[] b, int start) {
		return (short) (b[start] << 8 | (b[start + 1] & 0xff));
	}


	public static short shortFromByteArray(byte[] b, int start, int length) {
		switch (length) {
		case 0:
			return 0;

		case 1:
			return (short) (b[start] & 0xff);

		case 2:
		default:
			return (short) ((b[start] & 0xff) << 8 | (b[start + 1] & 0xff));
		}
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
				// extra break
				if (column == 8)
					sb.append(' ');

				sb.append(' ');
				sb.append(String.format("%02x", array[column + row]));
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


	public static void assertArrayEquals(byte[] expected, byte[] actual) {
		assertEquals(toHexDump(expected), toHexDump(actual));
	}


	/**
	 * Constructs a ByteTrie from the public, static, final fields of any enum
	 * that implements {@link PacketFragment}
	 */
	public static <T extends PacketFragment> ByteTrie<T> trieFromEnum(Class<T> klass) {
		ByteTrie<T> trie = new ByteTrie<T>();

		Field[] fields = klass.getDeclaredFields();
		for (Field field : fields) {
			int modifiers = field.getModifiers();

			if (Modifier.isFinal(modifiers) && Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
				try {
					Object object = field.get(null);
					if (klass.isInstance(object)) {
						@SuppressWarnings("unchecked")
						T value = (T) object;
						trie.add(value.getBytes(), value);
					} else
						throw new InvalidFieldError(field, " is not an instance of " + klass);
				} catch (IllegalArgumentException e) {
					throw new InvalidFieldError(field, e);
				} catch (IllegalAccessException e) {
					throw new InvalidFieldError(field, e);
				}
			}
		}

		return trie;
	}


	public static String fieldName(Field field) {
		return "Field " + field.getDeclaringClass().getCanonicalName() + ":" + field.getName();
	}


	public static int byteArrayLength(byte[] bytes) {
		return bytes.length;
	}


	public static byte[] extractBytes(byte[] bytes, int start) {
		return extractBytes(bytes, start, bytes.length - start);
	}


	public static byte[] extractBytes(byte[] bytes, int start, int length) {
		byte[] result = new byte[length];

		for (int i = 0; i < length; i++)
			result[i] = bytes[i + start];

		return result;
	}


	//
	// Types
	//

	public static final Class<? extends Object> BYTE_ARRAY;
	static {
		try {
			BYTE_ARRAY = Class.forName("[B");
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

}
