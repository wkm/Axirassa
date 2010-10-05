
package com.zanoccio.axirassa.overlord;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

		instances = count;
	}


	public int getInstances() {
		return instances;
	}


	public void execute() throws IOException {
		for (int i = 0; i < instances; i++) {
			System.out.printf("  %s %d -- %s\n", target.getName(), i, target.getTargetClass().getCanonicalName());
			executeInstance();
		}
	}


	private void executeInstance() throws IOException {
		ArrayList<String> cli = new ArrayList<String>();
		cli.add(overlord.getJava());

		// add jvm options
		if (target.getJVMOptions().size() > 0) {
			cli.addAll(target.getJVMOptions().getCommandLine());
		}

		cli.add(target.getTargetClass().getCanonicalName());

		ProcessBuilder processbuilder = new ProcessBuilder(cli);
		processbuilder.redirectErrorStream(true);
		processbuilder.directory(new File(overlord.getBaseDirectory()));

		Process process = processbuilder.start();

		BufferedReader stdoutstream = new BufferedReader(new InputStreamReader(process.getInputStream()));
		BufferedReader stderrstream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String line;
		while ((line = stdoutstream.readLine()) != null)
			System.out.println("STDOUT: " + line);

		while ((line = stderrstream.readLine()) != null)
			System.out.println("STDERR: " + line);

	}
}
