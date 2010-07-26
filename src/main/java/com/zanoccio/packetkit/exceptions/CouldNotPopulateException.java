
package com.zanoccio.packetkit.exceptions;

public class CouldNotPopulateException extends PacketKitException {
	private static final long serialVersionUID = 646904491014039469L;


	public CouldNotPopulateException(Object obj, Exception e) {
		super("Could not autopopulate field on " + obj.toString() + " because: " + e);
	}
}
