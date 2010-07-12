
package com.zanoccio.axirassa.webapp.components;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.components.TextField;

@SupportsInformalParameters
public class AxTextField extends AxAbstractTextInput {
	@Component(publishParameters = "annotationProvider,clientId,disabled,nulls,translate,validate,value")
	private TextField txtfield;
}
