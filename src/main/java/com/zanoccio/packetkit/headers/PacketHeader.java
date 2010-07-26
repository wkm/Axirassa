
package com.zanoccio.packetkit.headers;

import java.util.List;

import com.zanoccio.packetkit.exceptions.PacketKitException;

public interface PacketHeader {

	List<Byte> construct() throws PacketKitException;

}
