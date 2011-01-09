
package axirassa.webapp.components;

import org.apache.tapestry5.annotations.Parameter;

public class AxRadioGroup {
	@Parameter
	private Class<? extends AxRadioGroupEnumeration> enumeration;


	public Class<? extends AxRadioGroupEnumeration> getEnumeration() {
		return enumeration;
	}
}
