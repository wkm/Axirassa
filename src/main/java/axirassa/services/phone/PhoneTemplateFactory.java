
package axirassa.services.phone;

import org.antlr.stringtemplate.StringTemplate;

import axirassa.services.util.TemplateFactory;

public class PhoneTemplateFactory extends TemplateFactory<PhoneTemplate, PhoneTemplateType> {

	public static final PhoneTemplateFactory instance = new PhoneTemplateFactory();
	static {
		instance.buildInstances(PhoneTemplate.values(), PhoneTemplateType.values());
	}


	public static StringTemplate getTemplateInstance(PhoneTemplate template, PhoneTemplateType type) {
		return instance.getInstance(template, type);
	}

}
