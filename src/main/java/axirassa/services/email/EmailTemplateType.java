
package axirassa.services.email;

import axirassa.services.util.TemplateTypeEnumeration;

public enum EmailTemplateType implements TemplateTypeEnumeration {
	HTML(".html"),
	TEXT(".txt"),
	SUBJECT(".subject");

	private String extension;


	EmailTemplateType(String extension) {
		this.extension = extension;
	}


	@Override
	public String getExtension() {
		return extension;
	}
}
