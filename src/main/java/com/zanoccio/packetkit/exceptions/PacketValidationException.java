
package com.zanoccio.packetkit.exceptions;

import com.zanoccio.packetkit.PacketUtilities;
import com.zanoccio.packetkit.headers.PacketHeader;

public class PacketValidationException extends PacketKitException {
	private static final long serialVersionUID = 9048783654381441314L;


	public PacketValidationException(byte[] source, PacketHeader packet) {
		super("Packet " + packet.getClass().getCanonicalName() + " was invalid when deconstructed from bytes:\n"
		        + PacketUtilities.toHexDump(source));
	}

}
