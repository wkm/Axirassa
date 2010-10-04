
package com.zanoccio.axirassa.overlord;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.zanoccio.axirassa.overlord.exceptions.InvalidOverlordNameException;
import com.zanoccio.axirassa.overlord.exceptions.OverlordException;
import com.zanoccio.axirassa.overlord.exceptions.OverlordTargetClassNotFoundException;

public class ExecutionTarget {
	private final String name;
	private final Class<? extends Object> klass;


	public static ExecutionTarget create(Node node) throws OverlordException {
		if (!node.getNodeName().equalsIgnoreCase("target"))
			return null;

		NamedNodeMap attributes = node.getAttributes();

		String name, classname;

		Node namenode = attributes.getNamedItem("name");
		Node classnode = attributes.getNamedItem("class");

		name = namenode.getTextContent();
		classname = classnode.getTextContent();

		try {
			return new ExecutionTarget(name, classname);
		} catch (ClassNotFoundException e) {
			throw new OverlordTargetClassNotFoundException(name, node.getOwnerDocument(), e);
		}
	}


	public ExecutionTarget(String name, String classname) throws ClassNotFoundException, OverlordException {
		if (!OverlordUtilities.isValidName(name))
			throw new InvalidOverlordNameException(name);

		this.name = name;
		this.klass = Class.forName(classname);
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

}
