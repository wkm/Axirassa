
package com.zanoccio.axirassa.overlord;

import com.zanoccio.axirassa.overlord.exceptions.InvalidOverlordNameException;
import com.zanoccio.axirassa.overlord.exceptions.OverlordException;

public class ExecutionTarget {
	private final String name;
	private final Class<? extends Object> klass;
	private final TargetJVMOptions jvmoptions;


	public static String canonicalizeName(String name) {
		return name.toLowerCase();
	}


	public ExecutionTarget(String name, String classname) throws ClassNotFoundException, OverlordException {
		if (!OverlordUtilities.isValidName(name))
			throw new InvalidOverlordNameException(name);

		this.name = name;
		this.klass = Class.forName(classname);
		this.jvmoptions = new TargetJVMOptions();
	}


	/**
	 * @return the given name of this execution target
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the canonical name of this execution target; typically the
	 *         {@link #getName() name} in lower case.
	 * 
	 *         The canonical name must be unique within the configuration.
	 */
	public String getCanonicalName() {
		return name.toLowerCase();
	}


	public TargetJVMOptions getJVMOptions() {
		return jvmoptions;
	}


	public Class<? extends Object> getTargetClass() {
		return klass;
	}

}
