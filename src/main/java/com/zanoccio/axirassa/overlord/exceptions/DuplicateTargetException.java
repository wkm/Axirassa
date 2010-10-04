
package com.zanoccio.axirassa.overlord.exceptions;

import java.net.URL;

import com.zanoccio.axirassa.overlord.ExecutionTarget;

public class DuplicateTargetException extends OverlordException {
	private static final long serialVersionUID = 1177229272878048339L;


	public DuplicateTargetException(ExecutionTarget target, URL configfile) {
		super("A target with name " + target.getCanonicalName() + " already exists in " + configfile.getPath());
	}

}
