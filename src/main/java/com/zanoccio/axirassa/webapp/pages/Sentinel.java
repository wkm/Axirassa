
package com.zanoccio.axirassa.webapp.pages;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.json.JSONArray;

import com.zanoccio.axirassa.webapp.annotations.PublicPage;

@PublicPage
@Import(library = "${tapestry.scriptaculous}/prototype.js")
public class Sentinel {
	@Property
	@Persist
	private int clickCount;

	// @InjectComponent
	// private Request request;

	@InjectComponent
	private Zone counterZone;


	Object onActionFromClicker() {
		// if (request.isXHR())
		// // cleanly handle non-JS
		// return "Sentinel";

		clickCount++;
		JSONArray obj = new JSONArray();
		for (int i = 0; i < 30; i++)
			obj.put(new JSONArray(i, Math.ceil(Math.random() * 10)));

		return obj;
	}
}
