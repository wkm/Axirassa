
package com.zanoccio.axirassa.overlord;

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
	private static final String CONFIGURATION_FILE = "axoverlord.cfg.xml";


	public static void main(String[] parameters) throws OverlordException {
		Overlord overlord = new Overlord();
		overlord.execute(new String[] { "master" });
	}


	//
	// Class Instances
	//

	private final LinkedHashMap<String, ExecutionTarget> targets = new LinkedHashMap<String, ExecutionTarget>();
	private final LinkedHashMap<String, ExecutionGroup> groups = new LinkedHashMap<String, ExecutionGroup>();
	private Document dom;


	public void execute(String[] parameters) throws OverlordException {
		// locate configuration file
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		URL configfile = ClassLoader.getSystemResource(CONFIGURATION_FILE);
		if (configfile == null)
			throw new NoOverlordConfigurationException(CONFIGURATION_FILE);

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
			ExecutionTarget target = ExecutionTarget.create(targetlist.item(i));

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
	}


	/**
	 * @return the target with the given canonical name or <tt>null</tt> if no
	 *         such target exists.
	 */
	public ExecutionTarget findTarget(String targetname) {
		return targets.get(targetname.toLowerCase());
	}
}
