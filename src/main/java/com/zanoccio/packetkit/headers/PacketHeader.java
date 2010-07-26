
package com.zanoccio.packetkit.headers;

import java.util.List;

import com.zanoccio.packetkit.exceptions.JPacketException;

public interface PacketHeader {

	List<Byte> construct() throws JPacketException;

}
