
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


	//
	// Accessors
	//

	public void setVersion(int version) {
		this.version = version;
	}


	public int getVersion() {
		return version;
	}


	public void setHeaderLength(int headerlength) {
		this.headerlength = headerlength;
	}


	public int getHeaderLength() {
		return headerlength;
	}


	public void setServices(int services) {
		this.services = services;
	}


	public int getServices() {
		return services;
	}


	public void setTotalLength(int totallength) {
		this.totallength = totallength;
	}


	public int getTotalLength() {
		return totallength;
	}


	public int getFlags() {
		return flags;
	}


	public void setFlags(int flags) {
		this.flags = flags;
	}


	public int getFragmentOffset() {
		return fragmentoffset;
	}


	public void setFragmentOffset(int fragmentoffset) {
		this.fragmentoffset = fragmentoffset;
	}


	public int getTimeToLive() {
		return timetolive;
	}


	public void setTimeToLive(int timetolive) {
		this.timetolive = timetolive;
	}


	public IPProtocol getProtocol() {
		return protocol;
	}


	public void setProtocol(IPProtocol protocol) {
		this.protocol = protocol;
	}


	public int getChecksum() {
		return checksum;
	}


	public void setChecksum(int checksum) {
		this.checksum = checksum;
	}


	public IP4Address getSource() {
		return source;
	}


	public void setSource(IP4Address source) {
		this.source = source;
	}


	public IP4Address getDestination() {
		return destination;
	}


	public void setDestination(IP4Address destination) {
		this.destination = destination;
	}


	public PacketHeader getSubpacket() {
		return subpacket;
	}


	public void setSubpacket(PacketHeader subpacket) {
		this.subpacket = subpacket;
	}

}
