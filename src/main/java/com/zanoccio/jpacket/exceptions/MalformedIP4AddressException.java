
package com.zanoccio.jpacket.exceptions;

public class MalformedIP4AddressException extends JPacketException {
	private static final long serialVersionUID = 4524638855168726329L;


	public MalformedIP4AddressException(byte[] bytes) {
		super("Malformed IPv4 address byte sequence: " + bytes);
	}
}
