
package axirassa.services.exceptions;


public class UnknownTemplateError extends Error {
	private static final long serialVersionUID = -5671434207018829211L;


	public UnknownTemplateError(Object template) {
		super("Cannot find equivalent string template for " + template);
	}
}
