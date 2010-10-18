
package com.zanoccio.axirassa.overlord;

import java.io.IOException;
import java.net.URL;

import com.zanoccio.axirassa.overlord.exceptions.NoOverlordConfigurationException;
import com.zanoccio.axirassa.overlord.exceptions.OverlordException;

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
		platform = findSystemPlatform();
	}

	private static final String CONFIGURATION_FILE = "axoverlord.cfg.xml";


	public static SystemPlatform findSystemPlatform() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("windows") >= 0)
			return SystemPlatform.WINDOWS;
		else if (os.indexOf("linux") >= 0)
			return SystemPlatform.LINUX;
		else
			return SystemPlatform.OTHER;
	}


	public static String findJavaExecutable() {
		switch (platform) {
		case WINDOWS:
			return "java.exe";

		case LINUX:
			return "java";

		default:
			return null;
		}
	}


	public static void main(String[] parameters) throws OverlordException, IOException {
		Overlord overlord = new Overlord();
		overlord.execute(new String[] { "master" });
	}


	//
	// Class Instances
	//
	private OverlordConfiguration configuration;


	public void execute(String[] parameters) throws OverlordException, IOException {
		if (platform == SystemPlatform.OTHER) {
			System.err.println("Overlord is not supported on " + System.getProperty("os.name"));
			return;
		}

		URL configfile = ClassLoader.getSystemResource(CONFIGURATION_FILE);
		if (configfile == null)
			throw new NoOverlordConfigurationException(CONFIGURATION_FILE);

		configuration = new OverlordConfiguration();
		configuration.setJavaExecutable(findJavaExecutable());

		XMLConfigurationParser configparser = new XMLConfigurationParser(configfile, configuration);
		configparser.parse();

		for (String groupname : parameters) {
			ExecutionGroup group = configuration.getExecutionGroup(groupname);

			if (group != null)
				group.execute();
		}
	}
}
