
package com.zanoccio.jpacket.mock;

import com.zanoccio.jpacket.IP4Address;
import com.zanoccio.jpacket.MACAddress;
import com.zanoccio.jpacket.NetworkInterface;

public class MockNetworkInterface extends NetworkInterface {
	public MockNetworkInterface() {
		ipaddress = new IP4Address((byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78);
		macaddress = MACAddress.parse("DE:AD:DE:AD:BE:EF");
	}
}
