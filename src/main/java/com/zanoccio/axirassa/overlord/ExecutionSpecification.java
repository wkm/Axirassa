
package com.zanoccio.axirassa.overlord;

public class ExecutionSpecification {
	private int instances;
	private ExecutionTarget target;


	public ExecutionSpecification(ExecutionTarget target) {
		this.target = target;
	}


	public void setInstances(int count) {
		if (count < 1)
			return;

		this.instances = count;
	}


	public int getInstances() {
		return instances;
	}
}
