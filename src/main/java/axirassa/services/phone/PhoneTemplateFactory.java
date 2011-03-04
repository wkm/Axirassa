
package axirassa.services.phone;

import java.io.IOException;

import axirassa.services.util.TemplateFactory;
import freemarker.template.Template;

public class PhoneTemplateFactory extends TemplateFactory<PhoneTemplate, PhoneTemplateType> {

	public PhoneTemplateFactory() throws IOException {
		super(PhoneTemplate.BASE_LOCATION);
	}


	public static final PhoneTemplateFactory instance;
	static {
		try {
			instance = new PhoneTemplateFactory();
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		}
	}


	public static Template getTemplateInstance(PhoneTemplate template, PhoneTemplateType type) throws IOException {
		return instance.getTemplate(template, type);
	}

}
