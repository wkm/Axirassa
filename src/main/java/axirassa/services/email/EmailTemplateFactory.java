
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
			InputStream stream = System.class.getResourceAsStream(name.getFullLocation());
			if (stream == null)
				throw new ExceptionInInitializerError("Cannot find resource: " + name.getFullLocation());

			String templateBody;
			try {
				templateBody = StringUtilities.stringFromStream(stream);
			} catch (IOException e) {
				throw new ExceptionInInitializerError(e);
			}

			StringTemplate template = new StringTemplate(templateBody, DefaultTemplateLexer.class);
			instance.addStringTemplate(name, template);
		}
	}


	public static StringTemplate getTemplateInstance(EmailTemplate template) {
		return instance.getInstance(template);
	}


	//
	// CLASS INSTANCES
	//

	private final HashMap<EmailTemplate, StringTemplate> templates = new HashMap<EmailTemplate, StringTemplate>();


	public void addStringTemplate(EmailTemplate name, StringTemplate template) {
		templates.put(name, template);
	}


	public StringTemplate getInstance(EmailTemplate template) {
		StringTemplate baseTemplate = templates.get(template);

		if (baseTemplate == null)
			throw new UnknownTemplateError(template);

		return baseTemplate.getInstanceOf();
	}
}
