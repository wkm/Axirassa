
package com.zanoccio.axirassa.overlord;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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

		ExecutionSpecification spec = new ExecutionSpecification(overlord, target);
		spec.setInstances(instancecount);

		return spec;
	}


	//
	// Class Instances
	//

	private final Overlord overlord;
	private int instances;
	private final ExecutionTarget target;


	public ExecutionSpecification(Overlord overlord, ExecutionTarget target) {
		this.overlord = overlord;
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


	public void execute() throws IOException {
		for (int i = 0; i < instances; i++)
			System.out.printf("  %s %d -- %s\n", target.getName(), i, target.getTargetClass().getCanonicalName());
		executeInstance();
	}


	private void executeInstance() throws IOException {
		ProcessBuilder processbuilder = new ProcessBuilder("java.exe", target.getTargetClass().getCanonicalName());
		processbuilder.redirectErrorStream(true);
		processbuilder.directory(new File(overlord.getBaseDirectory()));

		System.out.println("starting from: " + processbuilder.directory());

		Process process = processbuilder.start();

		BufferedReader stream = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		while ((line = stream.readLine()) != null)
			System.out.println("OUT: " + line);

	}
}
