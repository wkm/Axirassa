
package com.zanoccio.axirassa.overlord.exceptions;


import org.w3c.dom.Document;

public class NoGroupsException extends OverlordException {

	private static final long serialVersionUID = 1377080776527568041L;


	public NoGroupsException(Document doc) {
		super("No execution groups found in " + doc.getBaseURI());
	}
}
