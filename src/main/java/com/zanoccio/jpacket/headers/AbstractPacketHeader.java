
package com.zanoccio.jpacket.headers;

import java.lang.reflect.Field;
import java.util.List;
import java.util.PriorityQueue;

public abstract class AbstractPacketHeader implements PacketHeader {

	@Override
	public List<Byte> construct() {
		Field[] fields = getClass().getFields();
		PriorityQueue<Field> queue = new PriorityQueue<Field>();
		
		for(field : fields) {
			
		}
	}
}
