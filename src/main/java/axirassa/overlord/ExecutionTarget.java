
package axirassa.overlord;

import test.axirassa.overlord.InvalidOverlordNameException;
import test.axirassa.overlord.OverlordException;
import lombok.Getter;
import lombok.Setter;

public class ExecutionTarget {
	@Getter
	private final String name;

	@Getter
	private final Class<? extends Object> targetClass;

	@Setter
	@Getter
	private ExecutionTargetOptions options;

	@Setter
	@Getter
	private boolean autoRestart;


	public static String canonicalizeName (String name) {
		return name.toLowerCase();
	}


	public ExecutionTarget (String name, String classname) throws ClassNotFoundException, OverlordException {
		if (!OverlordUtilities.isValidName(name))
			throw new InvalidOverlordNameException(name);

		this.name = name;
		this.targetClass = Class.forName(classname);
		this.options = new ExecutionTargetOptions();
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
}
