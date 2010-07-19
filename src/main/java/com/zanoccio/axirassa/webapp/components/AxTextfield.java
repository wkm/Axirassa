
package com.zanoccio.axirassa.webapp.components;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.TextField;

public class AxTextField extends AxAbstractTextInput {
	@Component(publishParameters = "annotationProvider,clientId,disabled,nulls,translate,validate,value")
	private TextField txtfield;


	@Override
	public String getControlName() {
		return txtfield.getControlName();
	}


	@Override
	public boolean isDisabled() {
		return txtfield.isDisabled();
	}


	@Override
	public boolean isRequired() {
		return txtfield.isRequired();
	}


	@Override
	public String getClientId() {
		return txtfield.getClientId();
	}
}
