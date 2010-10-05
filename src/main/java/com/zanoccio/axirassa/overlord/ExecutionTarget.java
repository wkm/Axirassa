
package com.zanoccio.axirassa.overlord;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.zanoccio.axirassa.overlord.exceptions.InvalidOverlordNameException;
import com.zanoccio.axirassa.overlord.exceptions.OverlordException;
import com.zanoccio.axirassa.overlord.exceptions.OverlordTargetClassNotFoundException;

public class ExecutionTarget {
	private final Overlord overlord;
	private final String name;
	private final Class<? extends Object> klass;
	private final TargetJVMOptions jvmoptions;


	public static ExecutionTarget create(Overlord overlord, Node node) throws OverlordException {
		if (!node.getNodeName().equalsIgnoreCase("target"))
			return null;

		// create the base execution target
		NamedNodeMap attributes = node.getAttributes();

		String name, classname;

		Node namenode = attributes.getNamedItem("name");
		Node classnode = attributes.getNamedItem("class");

		name = namenode.getTextContent();
		classname = classnode.getTextContent();

		ExecutionTarget target = null;

		try {
			target = new ExecutionTarget(overlord, name, classname);
		} catch (ClassNotFoundException e) {
			throw new OverlordTargetClassNotFoundException(name, node.getOwnerDocument(), e);
		}

		// apply any options
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);

			if (child.getNodeName().equals("jvmoptions")) {
				TargetJVMOptions.populate(target.getJVMOptions(), child);
			}
		}

		return target;
	}


	public ExecutionTarget(Overlord overlord, String name, String classname) throws ClassNotFoundException,
	        OverlordException {
		if (!OverlordUtilities.isValidName(name))
			throw new InvalidOverlordNameException(name);

		this.overlord = overlord;
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
