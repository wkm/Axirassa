
package com.zanoccio.axirassa.overlord.exceptions;

import org.w3c.dom.Document;


public class EmptyExecutionGroupException extends OverlordException {
	private static final long serialVersionUID = 7369994147681607551L;


	public EmptyExecutionGroupException(String name, Document document) {
		super(name + " execution group is empty in " + document.getBaseURI());
	}

}
