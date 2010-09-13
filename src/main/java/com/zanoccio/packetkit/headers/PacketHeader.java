
package com.zanoccio.packetkit.headers;

import com.zanoccio.packetkit.ByteParser;
import com.zanoccio.packetkit.NetworkInterface;
import com.zanoccio.packetkit.exceptions.PacketKitException;

public interface PacketHeader {

	public void associate(NetworkInterface networkinterface);


	byte[] construct() throws PacketKitException;


	public boolean deconstruct(byte[] bytes) throws PacketKitException;


	public boolean deconstruct(ByteParser bytes, int length) throws PacketKitException;

}
