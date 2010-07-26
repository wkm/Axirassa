
package com.zanoccio.packetkit.exceptions;

public class NoNetworkInterfaceException extends PacketKitException {
	private static final long serialVersionUID = 262437506957702591L;


	public NoNetworkInterfaceException(Object obj) {
		super("No network interface is associated with: " + obj);
	}
}
