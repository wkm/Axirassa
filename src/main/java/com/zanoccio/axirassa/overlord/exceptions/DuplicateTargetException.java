
package com.zanoccio.axirassa.overlord.exceptions;

import org.w3c.dom.Document;

import com.zanoccio.axirassa.overlord.ExecutionTarget;

public class DuplicateTargetException extends OverlordException {
	private static final long serialVersionUID = 1177229272878048339L;


	public DuplicateTargetException(ExecutionTarget target, Document doc) {
		super("A target with name " + target.getCanonicalName() + " already exists in " + doc.getBaseURI());
	}

}
