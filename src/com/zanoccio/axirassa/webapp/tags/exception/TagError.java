
package com.zanoccio.axirassa.webapp.tags.exception;

import com.zanoccio.axirassa.webapp.tags.AxirassaTag;

public abstract class TagError extends Error {
	private static final long serialVersionUID = -3910019735995139218L;


	public TagError(AxirassaTag tag, Exception e) {
		super("In :" + tag + " received: \n" + e);
	}

}
