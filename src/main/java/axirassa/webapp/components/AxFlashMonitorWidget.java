
package axirassa.webapp.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

import axirassa.webapp.pages.monitor.DataMonitor;

@Import(library = { "context:/js/amcharts/flash/swfobject.js" })
public class AxFlashMonitorWidget {
	@Inject
	private PageRenderLinkSource linkSource;

	@Property
	@Parameter(required = true, defaultPrefix = "literal")
	private Long pingerId;

	@Property
	@Parameter("context:/js/amcharts/flash/")
	private String path;

	@Property
	@Parameter("context:/js/amcharts/flash/amline.swf")
	private String swfObjectPath;

	@Property
	@Parameter("context:/js/amcharts/flash/expressInstall.swf")
	private String swfExpressInstall;

	@Property
	@Parameter("context:/js/data/axmonitorwidget_settings.xml")
	private String settingsXml;

	@Property
	private String dataUrl;

	@Inject
	private ComponentResources resources;


	private void setupRender() {
		Link link = linkSource.createPageRenderLinkWithContext(DataMonitor.class);
		dataUrl = link.toAbsoluteURI() + ":csv/" + pingerId;
	}
}
