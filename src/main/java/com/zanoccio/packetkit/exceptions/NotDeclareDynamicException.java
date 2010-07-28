
package com.zanoccio.packetkit.exceptions;

public class NotDeclareDynamicException extends PacketKitException {
	private static final long serialVersionUID = 1L;


	public NotDeclareDynamicException(Class<? extends Object> klass) {
		super(klass.getCanonicalName() + " is dynamically sized, but isn't annotated as such");
	}

}
