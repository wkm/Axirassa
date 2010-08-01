
package com.zanoccio.packetkit.headers;

import com.zanoccio.packetkit.IP4Address;
import com.zanoccio.packetkit.headers.annotations.Checksum;
import com.zanoccio.packetkit.headers.annotations.FromNetworkInterface;
import com.zanoccio.packetkit.headers.annotations.HeaderLength;
import com.zanoccio.packetkit.headers.annotations.StaticFragment;
import com.zanoccio.packetkit.headers.annotations.Subfragment;

/**
 * Internet Protocol (IP) Header
 * http://www.networksorcery.com/enp/protocol/ip.htm
 * 
 * @author wiktor
 */
public class IPHeader extends AbstractPacketHeader {

	@StaticFragment(slot = 0, size = 1, fixed = true)
	int version;

	@StaticFragment(slot = 1, size = 1, fixed = true)
	int headerlength;

	@StaticFragment(slot = 2, size = 1, fixed = true)
	int services;

	@StaticFragment(slot = 3, size = 2, fixed = true)
	@HeaderLength
	int totallength;

	@StaticFragment(slot = 4, size = 1, fixed = true)
	int flags;

	@StaticFragment(slot = 5, size = 2, fixed = true)
	int fragmentoffset;

	@StaticFragment(slot = 6, size = 1, fixed = true)
	int timetolive;

	@StaticFragment(slot = 7)
	IPProtocol protocol;

	@StaticFragment(slot = 8, size = 2, fixed = true)
	@Checksum(type = ChecksumMethod.ONESCOMPLEMENT)
	int checksum;

	@StaticFragment(slot = 9)
	@FromNetworkInterface
	IP4Address source;

	@StaticFragment(slot = 10)
	IP4Address destination;

	@Subfragment
	PacketHeader subpacket;

}
