
package com.zanoccio.axirassa.webapp.exceptions;

public class ExceptionInActionError extends Error {

	private static final long serialVersionUID = 4463187419054879948L;


	public ExceptionInActionError(String string, Exception e) {
		super(string + "\n" + e);
	}

}
