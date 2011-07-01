
package axirassa.services.email;

import lombok.Getter;
import axirassa.services.util.TemplateTypeEnumeration;

public enum EmailTemplateType implements TemplateTypeEnumeration {
	HTML("html"),
	TEXT("text"),
	SUBJECT("subject");

	@Getter
	private String extension;


	EmailTemplateType (String extension) {
		this.extension = extension;
	}
}
