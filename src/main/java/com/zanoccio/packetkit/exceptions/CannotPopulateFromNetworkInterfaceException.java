
package com.zanoccio.packetkit.exceptions;

import java.lang.reflect.Field;

public class CannotPopulateFromNetworkInterfaceException extends JPacketException {
	private static final long serialVersionUID = 8696498050689708393L;


	public CannotPopulateFromNetworkInterfaceException(Field field, Class<? extends Object> fieldtype) {
		super("Field: " + field.getDeclaringClass().getCanonicalName() + ":" + field.getName() + " with type "
		        + fieldtype + " cannot be populated from network interface");
	}
}
