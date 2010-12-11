
package packetkit.exceptions;

public class PacketKitException extends Exception {
	private static final long serialVersionUID = 7373749741008517553L;


	public PacketKitException(String msg) {
		super(msg);
	}


	public PacketKitException(ClassNotFoundException e) {
		this(e.toString());
	}
}
