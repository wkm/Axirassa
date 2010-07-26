
package com.zanoccio.jpacket.exceptions;

import java.lang.reflect.Field;

public class InvalidStaticFragmentTypeException extends JPacketException {
	private static final long serialVersionUID = 2682036002109015312L;


	public InvalidStaticFragmentTypeException(Field field) {
		super("Field " + field.getDeclaringClass().getCanonicalName() + ":" + field.getName()
		        + " is not a of a type that implements PacketFragment");
	}
}
