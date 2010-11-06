
package com.zanoccio.axirassa.overlord;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TargetJVMOptions {

	public enum JVMOption {
		XMX("Xmx"),
		XMS("Xms"),
		XSS("Xss");

		private final String value;


		JVMOption(String value) {
			this.value = value;
		}


		@Override
		public String toString() {
			return value;
		}
	}


	public static TargetJVMOptions populate(TargetJVMOptions options, Node jvmoptions) {
		if (!jvmoptions.getNodeName().equalsIgnoreCase("jvmoptions"))
			return null;

		NodeList optionnodes = jvmoptions.getChildNodes();

		for (int i = 0; i < optionnodes.getLength(); i++) {
			Node optionnode = optionnodes.item(i);

			if (!optionnode.getNodeName().equalsIgnoreCase("option"))
				continue;

			NamedNodeMap attributes = optionnode.getAttributes();

			Node namenode = attributes.getNamedItem("name");
			Node valuenode = attributes.getNamedItem("value");

			options.addOption(namenode.getTextContent(), valuenode.getTextContent());
		}

		return options;
	}


	//
	// Class Instances
	//

	private final LinkedHashMap<JVMOption, String> options = new LinkedHashMap<JVMOption, String>();


	public void addOption(String option, String value) {
		addOption(JVMOption.valueOf(option.toUpperCase()), value);
	}


	public void addOption(JVMOption option, String value) {
		options.put(option, value);
	}


	public List<String> getCommandLine() {
		ArrayList<String> cli = new ArrayList<String>();
		for (Entry<JVMOption, String> entry : options.entrySet()) {
			StringBuilder sb = new StringBuilder(30);
			sb.append('-').append(entry.getKey().toString()).append(entry.getValue());
			cli.add(sb.toString());
		}

		return cli;
	}


	/**
	 * @return the number of JVM options
	 */
	public int size() {
		return options.size();
	}
}
