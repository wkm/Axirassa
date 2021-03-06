
package packetkit.headers;

import packetkit.MACAddress;
import packetkit.headers.annotations.FromNetworkInterface;
import packetkit.headers.annotations.StaticFragment;

public class EthernetHeader extends AbstractPacketHeader {

	@StaticFragment(slot = 0)
	MACAddress destination;

	@StaticFragment(slot = 1)
	@FromNetworkInterface
	MACAddress source;

	@StaticFragment(slot = 2)
	EtherType type;


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


	public void setType(EtherType type) {
		this.type = type;
	}


	public EtherType getType() {
		return type;
	}

}
