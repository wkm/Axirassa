
package com.zanoccio.packetkit;

public interface PacketFragment {
	public byte[] getBytes();


	public PacketFragment fromBytes(byte[] buffer, int slot, int length);


	public int size();
}
