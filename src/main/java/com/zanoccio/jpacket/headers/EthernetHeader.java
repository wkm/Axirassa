
package com.zanoccio.jpacket.headers;

import org.jnetpcap.protocol.lan.Ethernet.EthernetType;

import com.zanoccio.jpacket.MACAddress;
import com.zanoccio.jpacket.headers.annotations.FromNetworkInterface;
import com.zanoccio.jpacket.headers.annotations.StaticFragment;

public class EthernetHeader extends AbstractPacketHeader {

	@StaticFragment(slot = 0)
	private MACAddress destination;

	@StaticFragment(slot = 1)
	@FromNetworkInterface
	private MACAddress source;

	@StaticFragment(slot = 2)
	private EthernetType type;


	public static EthernetHeader broadcast() {
		EthernetHeader header = new EthernetHeader();
		header.setDestination(MACAddress.BROADCAST);

		return header;
	}


	//
	// Accessors
	//

	public void setDestination(MACAddress destination) {
		this.destination = destination;
	}


	public MACAddress getDestination() {
		return destination;
	}


	public void setSource(MACAddress source) {
		this.source = source;
	}


	public MACAddress getSource() {
		return source;
	}


	public void setType(EthernetType type) {
		this.type = type;
	}


	public EthernetType getType() {
		return type;
	}

}
