
package com.zanoccio.packetkit.headers;

import com.zanoccio.packetkit.NetworkInterface;
import com.zanoccio.packetkit.exceptions.PacketKitException;

public interface PacketHeader {

	public void associate(NetworkInterface networkinterface);


	byte[] construct() throws PacketKitException;

}
