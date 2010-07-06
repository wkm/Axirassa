
package com.zanoccio.axirassa.webapp.tags.exception;

import com.zanoccio.axirassa.webapp.tags.AxirassaTag;

public class ExceptionInTagError extends TagError {
	private static final long serialVersionUID = 8914227787128450331L;


	public ExceptionInTagError(AxirassaTag tag, Exception e) {
		super(tag, e);
	}

}
