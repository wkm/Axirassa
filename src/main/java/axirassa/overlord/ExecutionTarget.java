
package axirassa.overlord;

import axirassa.overlord.exceptions.InvalidOverlordNameException;
import axirassa.overlord.exceptions.OverlordException;

public class ExecutionTarget {
	private final String name;
	private final Class<? extends Object> klass;
	private ExecutionTargetOptions options;
	private boolean autoRestart;


	public static String canonicalizeName (String name) {
		return name.toLowerCase();
	}


	public ExecutionTarget (String name, String classname) throws ClassNotFoundException, OverlordException {
		if (!OverlordUtilities.isValidName(name))
			throw new InvalidOverlordNameException(name);

		this.name = name;
		this.klass = Class.forName(classname);
		this.options = new ExecutionTargetOptions();
	}


	/**
	 * @return the given name of this execution target
	 */
	public String getName () {
		return name;
	}


	/**
	 * @return the canonical name of this execution target; typically the
	 *         {@link #getName() name} in lower case.
	 * 
	 *         The canonical name must be unique within the configuration.
	 */
	public String getCanonicalName () {
		return name.toLowerCase();
	}


	public ExecutionTargetOptions getOptions () {
		return options;
	}


	public Class<? extends Object> getTargetClass () {
		return klass;
	}


	public void setOptions (ExecutionTargetOptions jvmoptions) {
		this.options = jvmoptions;
	}


	public boolean isAutoRestart () {
		return autoRestart;
	}


	public void setAutoRestart (boolean autoRestart) {
		this.autoRestart = autoRestart;
	}

}
