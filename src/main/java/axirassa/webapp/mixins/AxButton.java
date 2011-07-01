
package axirassa.webapp.mixins;

import lombok.Getter;
import lombok.Setter;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;

import axirassa.webapp.data.AxButtonStyle;

@Import(stylesheet = { "context:/css/axbutton.css" })
@MixinAfter
public class AxButton {

	@Getter 
	@Setter
	private boolean tight;

	@Parameter(value = "default", defaultPrefix=BindingConstants.LITERAL)
	@Getter 
	@Setter
	private AxButtonStyle styling;


	void beginRender (MarkupWriter writer) {
		StringBuilder sb = new StringBuilder("button");
		
		if(tight) {
			sb.append(" axbtight");
		}
		
		switch(styling) {
		case DEFAULT:
			break;
			
		case DARK:
			sb.append(" axbdark");
			break;
		}
		
		writer.element("div", "class", sb.toString());
		writer.element("div", "class", "innerbutton");
	}


	void afterRender (MarkupWriter writer) {
		writer.end(); // innerbutton
		writer.end(); // button
	}
}
