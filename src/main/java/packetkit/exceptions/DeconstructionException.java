
package packetkit.exceptions;

import java.lang.reflect.Field;

public class DeconstructionException extends PacketKitException {
	private static final long serialVersionUID = -3146783102101939143L;


	public DeconstructionException(byte[] bytes, Throwable e) {
		super("Exception when deconstruction " + bytes.length + " bytes: " + e);
	}


	public DeconstructionException(byte[] bytes, Field field, Object value) {
		super("Could not deconstruct: " + field.getName() + " received: " + value);
	}
}
