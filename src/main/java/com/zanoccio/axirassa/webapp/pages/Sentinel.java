
package com.zanoccio.axirassa.webapp.pages;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;

import com.zanoccio.axirassa.webapp.annotations.PublicPage;

@PublicPage
@Import(library = "${tapestry.scriptaculous}/prototype.js")
public class Sentinel {
	@Property
	@Persist
	private int clickCount;

	@InjectComponent
	private Zone counterZone;


	Object onActionFromClicker() {
		clickCount++;
		return counterZone.getBody();
	}
}
