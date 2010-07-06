
package com.zanoccio.axirassa.webapp.tags.exception;

import com.zanoccio.axirassa.webapp.tags.WebappTag;

public abstract class TagError extends Error {
	private static final long serialVersionUID = -3910019735995139218L;


	public TagError(WebappTag tag, Exception e) {
		super("In :" + tag + " received: \n" + e);
	}

}
