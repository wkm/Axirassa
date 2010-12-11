
package axirassa.overlord.exceptions;

import org.w3c.dom.Document;

public class OverlordTargetClassNotFoundException extends OverlordException {
	private static final long serialVersionUID = 6999981549913728118L;


	public OverlordTargetClassNotFoundException(String target, Document doc, ClassNotFoundException e) {
		super(target + " target class could not be found in " + doc.getBaseURI(), e);
	}
}
