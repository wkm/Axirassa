
package com.zanoccio.packetkit.headers;

import java.util.List;

import com.zanoccio.packetkit.NetworkInterface;
import com.zanoccio.packetkit.exceptions.PacketKitException;

public interface PacketHeader {

	public void associate(NetworkInterface networkinterface);


	List<Byte> construct() throws PacketKitException;

}
