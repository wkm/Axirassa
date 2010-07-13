
package com.zanoccio.axirassa.webapp.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.services.Heartbeat;

public class AxError {
	@Parameter(name = "for", required = true, allowNull = false, defaultPrefix = BindingConstants.COMPONENT)
	private Field field;

	@Parameter
	private String value;

	@Environmental
	private Heartbeat heartbeat;

	private Element labelElement;


	void beginRender(MarkupWriter writer) {
		final Field field = this.field;
		labelElement = writer.element("span", "class", "msg");

		Runnable command = new Runnable() {
			@Override
			public void run() {
				String fieldId = field.getClientId();
				labelElement.forceAttributes("id", fieldId + "-msg");
			}
		};

		heartbeat.defer(command);

		return;
	}


	void afterRender(MarkupWriter writer) {
		writer.write(value);
		writer.end();
	}
}
