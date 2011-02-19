
package axirassa.services.exceptions;

import axirassa.services.email.EmailTemplate;

public class UnknownTemplateError extends Error {
	private static final long serialVersionUID = -5671434207018829211L;


	public UnknownTemplateError(EmailTemplate template) {
		super("Cannot find equivalent string template for " + template);
	}
}
