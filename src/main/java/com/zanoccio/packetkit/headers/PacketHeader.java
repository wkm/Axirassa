
package com.zanoccio.packetkit.headers;

import com.zanoccio.packetkit.ByteParser;
import com.zanoccio.packetkit.NetworkInterface;
import com.zanoccio.packetkit.exceptions.PacketKitException;

public interface PacketHeader {

	/**
	 * Associate this packet with a network interface; many packet types have
	 * their fields autowired by the network interface.
	 */
	public void associate(NetworkInterface networkinterface);


	/**
	 * Constructs the packet, creating the byte array that can be sent directly
	 * through the network interface.
	 */
	byte[] construct() throws PacketKitException;


	public boolean deconstruct(byte[] bytes) throws PacketKitException;


	/**
	 * Populates this packet by deconstructing a byte array into the various
	 * fields of a packet.
	 * 
	 * @param container
	 * @throws PacketKitException
	 */
	public boolean deconstruct(ByteParser bytes, int length) throws PacketKitException;


	/**
	 * Validation function for the packet. Typically used by
	 * {@link #deconstruct(byte[])} to verify that the deconstructed packet is
	 * internally consistent.
	 * 
	 * @return
	 */
	public boolean validate();

}
