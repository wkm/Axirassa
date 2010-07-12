
package com.zanoccio.axirassa.webapp.services.validation;

import org.apache.tapestry5.BaseValidationDecorator;
import org.apache.tapestry5.CSSClassConstants;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.services.Environment;

/**
 * 
 * Based on:
 * http://piraya-blog.blogspot.com/2009/06/creating-custom-validationdecorator
 * -for.html
 * 
 * @author wiktor
 * 
 */
public class CustomValidationDecorator extends BaseValidationDecorator {
	private final MarkupWriter markupwriter;
	private final Environment environment;


	public CustomValidationDecorator(final Environment environment, final MarkupWriter markupwriter) {
		this.environment = environment;
		this.markupwriter = markupwriter;
	}


	@Override
	public void insideLabel(final Field field, final Element element) {
		if (field != null) {
			if (inError(field))
				element.addClassName(CSSClassConstants.ERROR);
			if (field.isRequired())
				element.addClassName("required");
		}
	}


	@Override
	public void insideField(final Field field) {
		if (field != null && inError(field)) {
			markupwriter.getElement().addClassName(CSSClassConstants.ERROR);
		}
	}


	@Override
	public void afterField(final Field field) {
		if (field != null) {
			ValidationTracker tracker = environment.peekRequired(ValidationTracker.class);
			if (tracker.inError(field)) {
				String error = tracker.getError(field);
				if (error != null && error.length() > 0) {
					markupwriter.element("span", "class", CSSClassConstants.ERROR);
					markupwriter.write(error);
					markupwriter.end();
				}
			}
		}
	}


	private boolean inError(final Field field) {
		System.out.println("Checking for errir in field" + field.toString());

		ValidationTracker tracker = environment.peekRequired(ValidationTracker.class);
		return tracker.inError(field);
	}
}
