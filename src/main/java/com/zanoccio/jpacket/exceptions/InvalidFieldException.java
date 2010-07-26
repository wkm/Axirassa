
package com.zanoccio.jpacket.exceptions;

import java.lang.reflect.Field;

public class InvalidFieldException extends JPacketException {
	private static final long serialVersionUID = 2642108117722904404L;


	public InvalidFieldException(Field field, Exception e) {
		super("Field " + field.getDeclaringClass().getCanonicalName() + ":" + field.getName() + " is invalid: " + e);
	}
}
