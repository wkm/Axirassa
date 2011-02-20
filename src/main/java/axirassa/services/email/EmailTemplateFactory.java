
package axirassa.services.email;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;

import zanoccio.javakit.StringUtilities;
import axirassa.services.exceptions.UnknownTemplateError;

public class EmailTemplateFactory {

	public static final EmailTemplateFactory instance = new EmailTemplateFactory();

	static {
		for (EmailTemplate name : EmailTemplate.values()) {
			for (EmailTemplateType type : EmailTemplateType.values()) {
				InputStream stream = System.class.getResourceAsStream(name.getFullLocation() + type);
				if (stream == null)
					throw new ExceptionInInitializerError("Cannot find resource: " + name.getFullLocation());

				String templateBody;
				try {
					templateBody = StringUtilities.stringFromStream(stream);
				} catch (IOException e) {
					throw new ExceptionInInitializerError(e);
				}

				StringTemplate template = new StringTemplate(templateBody, DefaultTemplateLexer.class);
				instance.addStringTemplate(name, type, template);
			}
		}
	}


	public static StringTemplate getTemplateInstance(EmailTemplate template, EmailTemplateType type) {
		return instance.getInstance(template, type);
	}


	//
	// CLASS INSTANCES
	//

	private final HashMap<EmailTemplateTypeKey, StringTemplate> templates = new HashMap<EmailTemplateTypeKey, StringTemplate>();


	public void addStringTemplate(EmailTemplate name, EmailTemplateType type, StringTemplate template) {
		templates.put(new EmailTemplateTypeKey(name, type), template);
	}


	public StringTemplate getInstance(EmailTemplate template, EmailTemplateType type) {
		StringTemplate baseTemplate = templates.get(new EmailTemplateTypeKey(template, type));

		if (baseTemplate == null)
			throw new UnknownTemplateError(template);

		return baseTemplate.getInstanceOf();
	}
}

class EmailTemplateTypeKey {
	private final EmailTemplate template;
	private final EmailTemplateType type;


	public EmailTemplateTypeKey(EmailTemplate template, EmailTemplateType type) {
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


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof EmailTemplateTypeKey))
			return false;
		EmailTemplateTypeKey other = (EmailTemplateTypeKey) obj;
		if (template != other.template)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
