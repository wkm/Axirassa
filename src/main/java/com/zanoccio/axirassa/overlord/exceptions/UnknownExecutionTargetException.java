
package com.zanoccio.axirassa.overlord.exceptions;

import org.w3c.dom.Document;

public class UnknownExecutionTargetException extends OverlordException {
	private static final long serialVersionUID = 2687444431719104176L;


	public UnknownExecutionTargetException(String name, Document doc) {
		super("No target named " + name + " found in " + doc.getBaseURI());
	}
}
