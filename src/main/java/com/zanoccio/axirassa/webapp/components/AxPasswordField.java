
package com.zanoccio.axirassa.webapp.components;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.PasswordField;

public class AxPasswordField extends AxAbstractTextInput {
	@Component(publishParameters = "annotationProvider,clientId,disabled,nulls,translate,validate,value")
	private PasswordField txtfield;
}
