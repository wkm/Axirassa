
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
	int version_length_pack;

	@StaticFragment(slot = 1, size = 1, fixed = true)
	int services;

	@StaticFragment(slot = 2, size = 2, fixed = true)
	@HeaderLength
	int totallength;

	@StaticFragment(slot = 3, size = 2, fixed = true)
	int identification;

	@StaticFragment(slot = 4, size = 2, fixed = true)
	int flags_fragmentoffset_pack;

	@StaticFragment(slot = 5, size = 1, fixed = true)
	int timetolive;

	@StaticFragment(slot = 6)
	IPProtocol protocol;

	@StaticFragment(slot = 7, size = 2, fixed = true)
	@Checksum(type = ChecksumMethod.ONESCOMPLEMENT)
	byte[] checksum;

	@StaticFragment(slot = 8)
	@FromNetworkInterface
	IP4Address source;

	@StaticFragment(slot = 9)
	IP4Address destination;

	@Subfragment
	PacketHeader subpacket;


	//
	// Accessors
	//

	public void setVersion(int version) {
		// wipe the existing version
		version_length_pack &= 0x0f;

		// set the new version
		version_length_pack |= (version << 4);
	}


	public int getVersion() {
		return this.version_length_pack;
	}


	/**
	 * The length of the header in bytes (automatically converted into 32 bit
	 * words within the header)
	 */
	public void setHeaderLength(int headerlength) {
		// wipe the existing length
		version_length_pack &= 0xf0;

		// set the new version
		version_length_pack |= (0x0f & (headerlength / 4));
	}


	/**
	 * The length of the header in bytes.
	 * 
	 * (note that the the length specified in the header is in terms of 32 bit
	 * words).
	 */
	public int getHeaderLength() {
		return 4 * this.version_length_pack;
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


	public void setIdentification(int identification) {
		this.identification = identification;
	}


	public int getIdentification() {
		return identification;
	}


	public int getFlags() {
		return flags_fragmentoffset_pack;
	}


	public void setFlags(int flags) {
		flags_fragmentoffset_pack = flags << 13;
	}


	public int getFragmentOffset() {
		return flags_fragmentoffset_pack;
	}


	public void setFragmentOffset(int fragmentoffset) {
		this.flags_fragmentoffset_pack = fragmentoffset;
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


	public byte[] getChecksum() {
		return checksum;
	}


	public void setChecksum(byte[] checksum) {
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
