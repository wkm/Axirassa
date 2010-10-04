
package com.zanoccio.axirassa.overlord;

import java.util.ArrayList;

public class ExecutionGroup {

	private final ArrayList<ExecutionSpecification> executionspecs = new ArrayList<ExecutionSpecification>();


	public void addExecutionSpecification(ExecutionSpecification spec) {
		executionspecs.add(spec);
	}
}
