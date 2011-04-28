
package axirassa.services.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

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


	public String getText(T template, TType type, Map<String, Object> attributes) throws TemplateException, IOException {
		StringWriter writer = new StringWriter();
		getTemplate(template, type).process(attributes, writer);
		return writer.toString();
	}

}
