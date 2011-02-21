
package axirassa.services.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;

import zanoccio.javakit.StringUtilities;
import axirassa.services.exceptions.UnknownTemplateError;

public abstract class TemplateFactory<Template extends TemplateEnumeration, TemplateType extends TemplateTypeEnumeration> {

	private final HashMap<TemplateTypeKey<Template, TemplateType>, StringTemplate> templates = new HashMap<TemplateTypeKey<Template, TemplateType>, StringTemplate>();


	public void buildInstances(Template[] templates, TemplateType[] types) {
		for (Template template : templates) {
			for (TemplateType type : types) {
				String templateLocation = template.getFullLocation() + type.getExtension();
				InputStream stream = System.class.getResourceAsStream(templateLocation);
				if (stream == null)
					throw new ExceptionInInitializerError("Cannot find resource: " + templateLocation);

				try {
					String templateBody = StringUtilities.stringFromStream(stream);
					StringTemplate strTemplate = new StringTemplate(templateBody, DefaultTemplateLexer.class);
					addStringTemplate(template, type, strTemplate);
				} catch (IOException e) {
					throw new ExceptionInInitializerError(e);
				}
			}
		}
	}


	public void addStringTemplate(Template template, TemplateType type, StringTemplate strTemplate) {
		templates.put(new TemplateTypeKey<Template, TemplateType>(template, type), strTemplate);
	}


	public StringTemplate getInstance(Template template, TemplateType type) {
		StringTemplate baseTemplate = templates.get(new TemplateTypeKey<Template, TemplateType>(template, type));

		if (baseTemplate == null)
			throw new UnknownTemplateError(template);

		return baseTemplate.getInstanceOf();
	}
}

class TemplateTypeKey<Template, TemplateType> {
	private final Template template;
	private final TemplateType type;


	public TemplateTypeKey(Template template, TemplateType type) {
		this.template = template;
		this.type = type;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((template == null) ? 0 : template.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}


	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TemplateTypeKey))
			return false;
		TemplateTypeKey other = (TemplateTypeKey) obj;
		if (template != other.template)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
