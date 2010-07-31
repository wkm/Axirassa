
package com.zanoccio.packetkit.exceptions;

import java.lang.reflect.Field;

import com.zanoccio.packetkit.PacketUtilities;

public class InvalidFieldError extends Error {
	private static final long serialVersionUID = 7591893875520409172L;


	public InvalidFieldError(Field field, String msg) {
		super(PacketUtilities.fieldName(field) + " is invalid: " + msg);
	}


	public InvalidFieldError(Field field, Exception e) {
		this(field, e.toString());
	}
}
