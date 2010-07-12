
package com.zanoccio.axirassa.webapp.components;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.components.TextField;

@SupportsInformalParameters
public class AxTextfield {
	@Component(publishParameters = "annotationProvider,clientId,disabled,label,nulls,translate,validate,value")
	private TextField txtfield;

	@Property
	private String text;

	// @Property
	// private String label;

}
