
package com.zanoccio.axirassa.webapp.components;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.Submit;

public class AxSubmit {
	@Component(publishParameters = "context,defer,disabled,event,image")
	private Submit submit;
}
