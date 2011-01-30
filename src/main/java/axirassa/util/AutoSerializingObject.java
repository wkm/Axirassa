
package axirassa.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

abstract public class AutoSerializingObject implements Serializable {
	private static final long serialVersionUID = -6381035456256744960L;


	public byte[] toBytes() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(baos);
		out.writeObject(this);
		out.close();

		return baos.toByteArray();
	}
}
