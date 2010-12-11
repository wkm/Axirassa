
package axirassa.overlord.exceptions;

import org.w3c.dom.Document;

import axirassa.overlord.ExecutionGroup;

public class DuplicateGroupException extends OverlordException {
	private static final long serialVersionUID = -3694134007608921881L;


	public DuplicateGroupException(ExecutionGroup group, Document doc) {
		super("A group named " + group.getCanonicalName() + " is already defined in " + doc.getBaseURI());
	}

}
