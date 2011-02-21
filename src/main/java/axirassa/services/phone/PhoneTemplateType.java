
package axirassa.services.phone;

import axirassa.services.util.TemplateTypeEnumeration;

public enum PhoneTemplateType implements TemplateTypeEnumeration {
	VOICE(".voice"),
	SMS(".sms");

	private String extension;


	PhoneTemplateType(String extension) {
		this.extension = extension;
	}


	@Override
	public String getExtension() {
		return extension;
	}
}
