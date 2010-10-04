
package com.zanoccio.axirassa.overlord;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.zanoccio.axirassa.overlord.exceptions.OverlordException;
import com.zanoccio.axirassa.overlord.exceptions.UnknownExecutionTargetException;

public class ExecutionSpecification {
	public static ExecutionSpecification create(Overlord overlord, Node item) throws OverlordException {
		if (!item.getNodeName().equalsIgnoreCase("execute"))
			return null;

		// get the target and instance count for this spec
		NamedNodeMap attributes = item.getAttributes();
		String targetname = attributes.getNamedItem("target").getTextContent();
		String instances = attributes.getNamedItem("instances").getTextContent();
		int instancecount = Integer.parseInt(instances);

		ExecutionTarget target = overlord.findTarget(targetname);

		if (target == null)
			throw new UnknownExecutionTargetException(targetname, item.getOwnerDocument());

		ExecutionSpecification spec = new ExecutionSpecification(target);
		spec.setInstances(instancecount);

		return spec;
	}


	//
	// Class Instances
	//

	private int instances;
	private final ExecutionTarget target;


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
