
package com.zanoccio.packetkit.exceptions;

import java.lang.reflect.Field;

import com.zanoccio.packetkit.PacketUtilities;

public class InvalidFieldException extends PacketKitException {
	private static final long serialVersionUID = 2642108117722904404L;


	public InvalidFieldException(Field field, String msg) {
		super(PacketUtilities.fieldName(field) + " is invalid: " + msg);
	}


	public InvalidFieldException(Field field, Exception e) {
		this(field, e.toString());
	}
}
