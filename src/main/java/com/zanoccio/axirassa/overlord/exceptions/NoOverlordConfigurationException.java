
package com.zanoccio.axirassa.overlord.exceptions;

public class NoOverlordConfigurationException extends OverlordException {
	private static final long serialVersionUID = 6986377956653206852L;


	public NoOverlordConfigurationException(String filename) {
		super("Could not locate configuration file: " + filename);
	}

}
