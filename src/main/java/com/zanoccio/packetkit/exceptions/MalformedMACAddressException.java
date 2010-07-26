
package com.zanoccio.packetkit.exceptions;

public class MalformedMACAddressException extends PacketKitException {
	private static final long serialVersionUID = -1244000509353195669L;


	public MalformedMACAddressException(String addr) {
		super("Malformed MAC address string: " + addr);
	}


	// TODO provide better formatting for the byte sequence
	public MalformedMACAddressException(byte[] bytes) {
		super("Malformed MAC address byte sequence: " + bytes);
	}
}
