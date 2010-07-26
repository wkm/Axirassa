
package com.zanoccio.jpacket.headers;

import java.util.List;

import com.zanoccio.jpacket.exceptions.JPacketException;

public interface PacketHeader {

	List<Byte> construct() throws JPacketException;

}
