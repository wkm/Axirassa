
package com.zanoccio.packetkit.exceptions;

import java.lang.reflect.Field;

public class SlotTakenException extends JPacketException {
	private static final long serialVersionUID = -7666863012723927197L;


	public SlotTakenException(Field f) {
		super("For field " + f.getDeclaringClass().getCanonicalName() + ":" + f.getName() + " slot is already taken");
	}
}
