
package com.zanoccio.axirassa.overlord;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.zanoccio.axirassa.overlord.exceptions.NoOverlordConfigurationException;
import com.zanoccio.axirassa.overlord.exceptions.OverlordException;
import com.zanoccio.axirassa.overlord.os.AbstractOverlordSystemSupport;
import com.zanoccio.axirassa.overlord.os.OverlordSystemSupport;

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


	public static void main(String[] parameters) throws OverlordException, IOException {
		Overlord overlord = new Overlord();
		overlord.execute(new String[] { "master" });
	}


	//
	// Class Instances
	//
	private OverlordConfiguration configuration;


	public void execute(String[] parameters) throws OverlordException, IOException {
		OverlordSystemSupport systemsupport = AbstractOverlordSystemSupport.getSystemSupport();

		URL configfile = ClassLoader.getSystemResource(CONFIGURATION_FILE);
		if (configfile == null)
			throw new NoOverlordConfigurationException(CONFIGURATION_FILE);

		InputStream configstream = ClassLoader.getSystemResourceAsStream(CONFIGURATION_FILE);

		System.out.println("CONFIG STREAM: " + configstream);

		configuration = new OverlordConfiguration();
		configuration.setJavaExecutable(systemsupport.getJavaExecutable());

		XMLConfigurationParser configparser = new XMLConfigurationParser(configfile, configstream, configuration);
		configparser.parse();

		for (String groupname : parameters) {
			ExecutionGroup group = configuration.getExecutionGroup(groupname);

			if (group != null)
				group.execute();
		}
	}
}
