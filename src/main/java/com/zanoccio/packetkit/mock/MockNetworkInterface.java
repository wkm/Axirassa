
package com.zanoccio.packetkit.mock;

import com.zanoccio.packetkit.IP4Address;
import com.zanoccio.packetkit.MACAddress;
import com.zanoccio.packetkit.NetworkInterface;
import com.zanoccio.packetkit.frames.Frame;

public class MockNetworkInterface extends NetworkInterface {
	public MockNetworkInterface() {
		ipaddress = new IP4Address((byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78);
		macaddress = MACAddress.parse("DE:AD:DE:AD:BE:EF");
	}


	@Override
	public void openLive() {
		// ignore
	}


	@Override
	public void sendPacket(Frame frame) {
		// ignore
	}
}