
package axirassa.overlord;

import java.io.IOException;
import java.util.ArrayList;

import lombok.Getter;
import axirassa.overlord.exceptions.InvalidOverlordNameException;
import axirassa.overlord.exceptions.OverlordException;

public class ExecutionGroup {

	public static String canonicalizeName(String name) {
		return name.toLowerCase();
	}


	//
	// Class Instances
	//

	@Getter
	private final String name;
	
	@Getter
	private final ArrayList<ExecutionSpecification> executionSpecifications = new ArrayList<ExecutionSpecification>();


	public ExecutionGroup(String name) throws OverlordException {
		if (!OverlordUtilities.isValidName(name))
			throw new InvalidOverlordNameException(name);

		this.name = name;
	}

	public String getCanonicalName() {
		return name.toLowerCase();
	}


	public void addExecutionSpecification(ExecutionSpecification spec) {
		if (spec == null)
			return;

		executionSpecifications.add(spec);
	}

	public void execute() throws IOException, InterruptedException {
		for (ExecutionSpecification spec : executionSpecifications)
			spec.execute();
	}

}
