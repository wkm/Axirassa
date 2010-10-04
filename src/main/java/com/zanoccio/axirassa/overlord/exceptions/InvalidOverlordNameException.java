
package com.zanoccio.axirassa.overlord.exceptions;

import com.zanoccio.axirassa.overlord.OverlordUtilities;

public class InvalidOverlordNameException extends OverlordException {
	private static final long serialVersionUID = 8883420722778435141L;


	public InvalidOverlordNameException(String msg) {
		this(msg, OverlordUtilities.VALID_NAME_PATTERN);
	}


	public InvalidOverlordNameException(String name, String pattern) {
		super("Names must match pattern " + pattern + " name given: " + name);
	}

}
