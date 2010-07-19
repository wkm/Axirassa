
package com.zanoccio.axirassa.webapp.components;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.services.Heartbeat;

public class AxTextField extends AxAbstractTextInput {
	@Component(publishParameters = "annotationProvider,clientId,disabled,nulls,translate,validate,value")
	private TextField txtfield;

	@Environmental
	private Heartbeat heartbeat;

	private Element labelelement;

	private String value;


	@BeginRender
	void beginRender(MarkupWriter writer) {
		final Field txtfield = this.txtfield;
		labelelement = writer.element("span", "class", "msg");

		Runnable command = new Runnable() {
			@Override
			public void run() {
				String fieldId = txtfield.getClientId();
				labelelement.forceAttributes("id", fieldId + "-msg");
			}
		};

		heartbeat.defer(command);

		return;
	}


	@AfterRender
	void afterRender(MarkupWriter writer) {
		writer.write(value);
		writer.end();
	}


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
