
package com.zanoccio.jpacket.headers;

import org.bouncycastle.util.IPAddress;

import com.zanoccio.jpacket.MACAddress;
import com.zanoccio.jpacket.headers.annotations.FromNetworkInterface;
import com.zanoccio.jpacket.headers.annotations.StaticFragment;

public class ARPHeader extends AbstractPacketHeader {

	@StaticFragment(slot = 0, size = 4)
	private ARPHardwareType hardwaretype;

	@StaticFragment(slot = 1, size = 4)
	private ARPProtocolType protocoltype;

	@StaticFragment(slot = 2, size = 2)
	private int hardwaresize;

	@StaticFragment(slot = 3, size = 2)
	private int protocolsize;

	@StaticFragment(slot = 4, size = 4)
	private ARPOpcode opcode;

	@StaticFragment(slot = 5)
	@FromNetworkInterface
	private MACAddress sendermac;

	@StaticFragment(slot = 6)
	@FromNetworkInterface
	private IPAddress senderip;

	@StaticFragment(slot = 7)
	private MACAddress targetmac;

	@StaticFragment(slot = 8)
	private IPAddress targetip;


	/**
	 * Constructs a whoHas ARP packet broadcast on the local network
	 * 
	 * @param addr
	 * @return
	 */
	public static ARPHeader whoHas(IPAddress addr) {
		ARPHeader header = new ARPHeader();
		header.setHardwareType(ARPHardwareType.ETHERNET);
		header.setProtocolType(ARPProtocolType.IP4);
		header.setHardwareSize(6);
		header.setProtocolSize(4);

		header.setTargetMAC(MACAddress.EMPTY);
		header.setTargetIP(addr);

		return header;
	}


	public ARPHeader() {
		setHardwareType(ARPHardwareType.ETHERNET);
		setProtocolType(ARPProtocolType.IP4);
		setHardwareSize(6); // for ETHERNET
		setProtocolSize(4); // for IP
	}


	public void setHardwareType(ARPHardwareType hardwaretype) {
		this.hardwaretype = hardwaretype;
	}


	public ARPHardwareType getHardwareType() {
		return hardwaretype;
	}


	public void setProtocolType(ARPProtocolType protocoltype) {
		this.protocoltype = protocoltype;
	}


	public ARPProtocolType getProtocolType() {
		return protocoltype;
	}


	public void setHardwareSize(int hardwaresize) {
		this.hardwaresize = hardwaresize;
	}


	public int getHardwareSize() {
		return hardwaresize;
	}


	public void setProtocolSize(int protocolsize) {
		this.protocolsize = protocolsize;
	}


	public int getProtocolSize() {
		return protocolsize;
	}


	public void setOpcode(ARPOpcode opcode) {
		this.opcode = opcode;
	}


	public ARPOpcode getOpcode() {
		return opcode;
	}


	public void setSenderMAC(MACAddress sendermac) {
		this.sendermac = sendermac;
	}


	public MACAddress getSenderMAC() {
		return sendermac;
	}


	public void setSenderIP(IPAddress senderip) {
		this.senderip = senderip;
	}


	public IPAddress getSenderIP() {
		return senderip;
	}


	public void setTargetMAC(MACAddress targetmac) {
		this.targetmac = targetmac;
	}


	public MACAddress getTargetMAC() {
		return targetmac;
	}


	public void setTargetIP(IPAddress targetip) {
		this.targetip = targetip;
	}


	public IPAddress getTargetIP() {
		return targetip;
	}

}
