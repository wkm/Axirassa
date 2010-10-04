
package com.zanoccio.axirassa.overlord.exceptions;

import java.net.URL;

import com.zanoccio.axirassa.overlord.ExecutionGroup;

public class DuplicateGroupException extends OverlordException {
	private static final long serialVersionUID = -3694134007608921881L;


	public DuplicateGroupException(ExecutionGroup group, URL file) {
		super("A group named " + group.getCanonicalName() + " is already definedin " + file.getPath());
	}

}
