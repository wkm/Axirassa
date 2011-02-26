
package axirassa.services.util;

import java.io.IOException;

import freemarker.template.Configuration;
import freemarker.template.Template;

public abstract class TemplateFactory<T extends TemplateEnumeration, TType extends TemplateTypeEnumeration> {

	public final static String TEMPLATE_ENCODING = "UTF-8";

	private final Configuration configuration = new Configuration();


	public TemplateFactory(String baseDirectory) throws IOException {
		configuration.setWhitespaceStripping(true);
		configuration.setClassForTemplateLoading(TemplateFactory.class, baseDirectory);
	}


	public Template getTemplate(T template, TType type) throws IOException {
		return configuration.getTemplate(getTemplateLocation(template, type), TEMPLATE_ENCODING);
	}


	private String getTemplateLocation(T template, TType type) {
		return template.getLocation() + "_" + type.getExtension() + ".ftl";
	}

}
