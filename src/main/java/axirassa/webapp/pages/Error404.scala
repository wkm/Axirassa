
package axirassa.webapp.pages;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.Request;

class Error404 {
	@Property
	@Inject
	var request : Request = _

	@Property
	@Inject
	@Symbol(SymbolConstants.PRODUCTION_MODE)
	var productionMode : Boolean = _
}
