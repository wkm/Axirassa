
package axirassa.services.phone;

import lombok.Getter;
import axirassa.services.util.TemplateTypeEnumeration;

public enum PhoneTemplateType implements TemplateTypeEnumeration {
	VOICE("voice"),
	SMS("sms");

	@Getter
	private String extension;


	PhoneTemplateType (String extension) {
		this.extension = extension;
	}
}
