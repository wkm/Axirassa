
package com.zanoccio.axirassa.overlord.exceptions;

import java.net.URL;

public class NoExecutionTargetsException extends OverlordException {
	private static final long serialVersionUID = 5328382156966446142L;


	public NoExecutionTargetsException(URL file) {
		super("No execution targets in configuration at: " + file.getPath());
	}
}
