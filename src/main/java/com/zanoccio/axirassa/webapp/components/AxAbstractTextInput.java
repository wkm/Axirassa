
package com.zanoccio.axirassa.webapp.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

abstract public class AxAbstractTextInput {

	@Inject
	private ComponentResources resources;

	@Property
	private String name;

	@Property
	private String text;

	@Parameter
	private String label;


	public AxAbstractTextInput() {
		super();
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getLabel() {
		if (label == null)
			return resources.getId();
		else
			return label;
	}

}