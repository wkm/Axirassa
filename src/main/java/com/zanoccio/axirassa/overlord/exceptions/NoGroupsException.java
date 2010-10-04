
package com.zanoccio.axirassa.overlord.exceptions;

import java.net.URL;


public class NoGroupsException extends OverlordException {

	private static final long serialVersionUID = 1377080776527568041L;


	public NoGroupsException(URL config) {
		super("No execution groups found in " + config.getPath());
	}
}
