
package com.zanoccio.axirassa.overlord;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.zanoccio.axirassa.overlord.exceptions.DuplicateGroupException;
import com.zanoccio.axirassa.overlord.exceptions.DuplicateTargetException;
import com.zanoccio.axirassa.overlord.exceptions.NoExecutionTargetsException;
import com.zanoccio.axirassa.overlord.exceptions.NoGroupsException;
import com.zanoccio.axirassa.overlord.exceptions.NoOverlordConfigurationException;
import com.zanoccio.axirassa.overlord.exceptions.OverlordException;
import com.zanoccio.axirassa.overlord.exceptions.OverlordParsingException;

/**
 * A process starting/monitoring daemon and framework.
 * 
 * AxOverlord hooks into a HornetQ message passing framework
 * 
 * @author wiktor
 * 
 */
public class Overlord {

	enum SystemPlatform {
		WINDOWS,
		LINUX,
		OTHER;
	}


	private static final SystemPlatform platform;
	static {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("windows") >= 0)
			platform = SystemPlatform.WINDOWS;
		else if (os.indexOf("linux") >= 0)
			platform = SystemPlatform.LINUX;
		else
			platform = SystemPlatform.OTHER;
	}

	private static final String CONFIGURATION_FILE = "axoverlord.cfg.xml";


	public static void main(String[] parameters) throws OverlordException, IOException {
		Overlord overlord = new Overlord();
		overlord.execute(new String[] { "master" });
	}


	//
	// Class Instances
	//

	private final LinkedHashMap<String, ExecutionTarget> targets = new LinkedHashMap<String, ExecutionTarget>();
	private final LinkedHashMap<String, ExecutionGroup> groups = new LinkedHashMap<String, ExecutionGroup>();
	private Document dom;

	private String basedirectory;


	public String getJava() {
		switch (platform) {
		case WINDOWS:
			return "java.exe";

		case LINUX:
			return "java";

		default:
			return null;
		}
	}


	public void execute(String[] parameters) throws OverlordException, IOException {
		if (platform == SystemPlatform.OTHER) {
			System.err.println("Ax|Overlord is not supported on " + System.getProperty("os.name"));
			return;
		}

		// locate configuration file
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		URL configfile = ClassLoader.getSystemResource(CONFIGURATION_FILE);
		if (configfile == null)
			throw new NoOverlordConfigurationException(CONFIGURATION_FILE);

		basedirectory = new File(configfile.getPath()).getParent();

		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(configfile.getPath());
		} catch (Exception e) { // TODO: much too general
			throw new OverlordParsingException(configfile, e);
		}

		Element docroot = dom.getDocumentElement();

		// create execution targets
		NodeList targetlist = docroot.getElementsByTagName("target");
		if (targetlist.getLength() < 1)
			throw new NoExecutionTargetsException(configfile);

		for (int i = 0; i < targetlist.getLength(); i++) {
			ExecutionTarget target = ExecutionTarget.create(this, targetlist.item(i));

			// check that this execution target doesn't already exist
			if (targets.containsKey(target.getCanonicalName()))
				throw new DuplicateTargetException(target, configfile);

			targets.put(target.getCanonicalName(), target);
		}
		targetlist = null;

		// create execution groups
		NodeList executionlist = docroot.getElementsByTagName("group");
		if (executionlist.getLength() < 1)
			throw new NoGroupsException(configfile);

		for (int i = 0; i < executionlist.getLength(); i++) {
			ExecutionGroup group = ExecutionGroup.create(this, executionlist.item(i));

			// check that this execution group doesn't already exist
			if (groups.containsKey(group.getCanonicalName()))
				throw new DuplicateGroupException(group, configfile);

			groups.put(group.getCanonicalName(), group);
		}

		// attempt to execute the specified execution groups
		for (String parameter : parameters) {
			ExecutionGroup group = groups.get(parameter);
			group.execute();
		}
		executionlist = null;
	}


	/**
	 * @return the target with the given canonical name or <tt>null</tt> if no
	 *         such target exists.
	 */
	public ExecutionTarget findTarget(String targetname) {
		return targets.get(targetname.toLowerCase());
	}


	public String getBaseDirectory() {
		return basedirectory;
	}
}
