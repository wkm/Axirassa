
package com.zanoccio.jpacket.headers;

import java.net.Inet4Address;
import java.util.Collection;

public class EthernetHeader extends AbstractPacketHeader {

	private Inet4Address destination;


	public Inet4Address getDestination() {
		return destination;
	}


	public void setDestination( destination) {
		this.destination = destination;
	}


	private Inet4Address source;


	public Inet4Address getSource() {
		return source;
	}


	public void setSource(Inet4Address source) {
		this.source = source;
	}


	@Override
    public Collection<Byte> construct() {
	    ArrayList<Byte> bytes = new ArrayList<Byte>(
    }
}
