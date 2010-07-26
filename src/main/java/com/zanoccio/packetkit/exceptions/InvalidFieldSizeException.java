
package com.zanoccio.packetkit.exceptions;

import java.lang.reflect.Field;

public class InvalidFieldSizeException extends PacketKitException {
	/**
     * 
     */
	private static final long serialVersionUID = 7256828966835034427L;


	public InvalidFieldSizeException(Field f, int size) {
		super("The field: " + f.getDeclaringClass().getCanonicalName() + ":" + f.getName() + " has an invalid width: "
		        + size);
	}

}
