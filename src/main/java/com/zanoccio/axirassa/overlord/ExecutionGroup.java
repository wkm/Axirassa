
package com.zanoccio.axirassa.overlord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.zanoccio.axirassa.overlord.exceptions.EmptyExecutionGroupException;
import com.zanoccio.axirassa.overlord.exceptions.InvalidOverlordNameException;
import com.zanoccio.axirassa.overlord.exceptions.OverlordException;

public class ExecutionGroup {

	public static ExecutionGroup create(Overlord overlord, Node node) throws OverlordException {
		if (!node.getNodeName().equalsIgnoreCase("group"))
			return null;

		// create an empty execution group
		NamedNodeMap attributes = node.getAttributes();

		String name;
		Node namenode = attributes.getNamedItem("name");
		name = namenode.getTextContent();

		ExecutionGroup group = new ExecutionGroup(name);

		// fill it with execution targets
		NodeList childnodes = node.getChildNodes();

		if (childnodes.getLength() < 1)
			throw new EmptyExecutionGroupException(name, node.getOwnerDocument());

		for (int i = 0; i < childnodes.getLength(); i++) {
			group.addExecutionSpecification(ExecutionSpecification.create(overlord, childnodes.item(i)));
		}

		return group;
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


	public void execute() throws IOException {
		System.out.println("Starting execution group: " + getName());
		for (ExecutionSpecification spec : executionspecs) {
			spec.execute();
		}
	}

}
