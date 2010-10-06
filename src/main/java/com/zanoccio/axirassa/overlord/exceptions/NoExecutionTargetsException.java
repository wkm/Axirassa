
package com.zanoccio.axirassa.overlord.exceptions;

import org.w3c.dom.Document;

public class NoExecutionTargetsException extends OverlordException {
	private static final long serialVersionUID = 5328382156966446142L;


	public NoExecutionTargetsException(Document doc) {
		super("No execution targets in configuration at: " + doc.getBaseURI());
	}
}
