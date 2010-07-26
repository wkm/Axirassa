
package com.zanoccio.jpacket.exceptions;

import java.lang.reflect.Field;

public class NullFieldException extends JPacketException {
	private static final long serialVersionUID = 7677106196022314575L;


	public NullFieldException(Field field) {
		super("Field " + field.getDeclaringClass().getCanonicalName() + ":" + field.getName() + " is set to null");
	}
}
