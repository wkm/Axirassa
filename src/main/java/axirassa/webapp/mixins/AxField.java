
package axirassa.webapp.mixins;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.corelib.base.AbstractTextField;
import org.apache.tapestry5.ioc.annotations.Inject;

@MixinAfter
public class AxField {

	@InjectContainer
	private AbstractTextField field;

	@Inject
	private ComponentResources componentResources;


	public void beginRender(MarkupWriter writer) {
		writer.attributes("class", "input txtinput");
	}


	public void afterRender(MarkupWriter writer) {
		// nothing for now
	}

}
