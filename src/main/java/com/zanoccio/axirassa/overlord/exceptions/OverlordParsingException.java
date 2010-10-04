
package com.zanoccio.axirassa.overlord.exceptions;

import java.net.URL;


public class OverlordParsingException extends OverlordException {
	private static final long serialVersionUID = 652109207291301473L;


	public OverlordParsingException(URL file, Exception exception) {
		super("Error while parsing " + file.getPath(), exception);
	}
}
