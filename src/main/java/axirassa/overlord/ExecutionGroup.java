
package axirassa.overlord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import axirassa.overlord.exceptions.InvalidOverlordNameException;
import axirassa.overlord.exceptions.OverlordException;

public class ExecutionGroup {

	public static String canonicalizeName(String name) {
		return name.toLowerCase();
	}


	//
	// Class Instances
	//

	private final String name;
	private final ArrayList<ExecutionSpecification> executionspecs = new ArrayList<ExecutionSpecification>();


	public ExecutionGroup(String name) throws OverlordException {
		if (!OverlordUtilities.isValidName(name))
			throw new InvalidOverlordNameException(name);

		this.name = name;
	}


	public String getName() {
		return name;
	}


	public String getCanonicalName() {
		return name.toLowerCase();
	}


	public void addExecutionSpecification(ExecutionSpecification spec) {
		if (spec == null)
			return;

		executionspecs.add(spec);
	}


	public List<ExecutionSpecification> getExecutionSpecifications() {
		return executionspecs;
	}


	public void execute() throws IOException, InterruptedException {
		for (ExecutionSpecification spec : executionspecs)
			spec.execute();
	}

}
