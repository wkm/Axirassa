
package axirassa.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

abstract public class AutoSerializingObjectOld implements Serializable {
	private static final long serialVersionUID = -6381035456256744960L;


	static public Object fromBytes (byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInput in = new ObjectInputStream(bais);
		Object object = in.readObject();
		in.close();

		return object;
	}


	public byte[] toBytes () throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(baos);
		out.writeObject(this);
		out.close();

		return baos.toByteArray();
	}
}
