
package packetkit;

public class ByteParser {
	public byte[] bytes;
	public int cursor;


	public ByteParser(byte[] bytes, int cursor) {
		this.bytes = bytes;
		this.cursor = cursor;
	}
}
