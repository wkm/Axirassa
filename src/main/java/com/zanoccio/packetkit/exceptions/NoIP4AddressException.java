
package com.zanoccio.packetkit.exceptions;

import org.jnetpcap.PcapIf;

public class NoIP4AddressException extends JPacketException {
	private static final long serialVersionUID = 2137358715470463928L;


	public NoIP4AddressException(PcapIf device) {
		super("Could not find IP4 address for: " + device.toString());
	}

}
