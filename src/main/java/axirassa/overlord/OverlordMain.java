
package axirassa.overlord;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Getter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import axirassa.overlord.exceptions.NoOverlordConfigurationException;
import axirassa.overlord.exceptions.OverlordException;
import axirassa.overlord.exceptions.UnknownExecutionTargetException;
import axirassa.overlord.os.AbstractOverlordSystemSupport;
import axirassa.overlord.os.OverlordSystemSupport;
import axirassa.overlord.tui.OverlordWindow;

/**
 * A process starting/monitoring daemon and framework.
 * 
 * AxOverlord hooks into a HornetQ message passing framework.
 * 
 * Note that java processes started by Overlord will not be terminated by
 * default unless {@link #addShutdownHooks()} is called.
 * 
 * @author wiktor
 * 
 */
public class OverlordMain {
	public static final Logger logger = LoggerFactory.getLogger(OverlordMain.class);
	private static final String CONFIGURATION_FILE = "axoverlord.cfg.xml";

	private Integer execId = 0;


	public int getNextExecID () {
		synchronized (execId) {
			execId++;
			return execId;
		}
	}


	public static void main (String[] parameters) throws OverlordException, IOException, InterruptedException {
		OverlordMain overlord = new OverlordMain();
		overlord.addShutdownHooks();

		overlord.init();

		OverlordWindow window = new OverlordWindow(overlord);
		window.show();

		//
		// if (parameters.length <= 0)
		// overlord.execute(new String[] { "master" });
		// else
		// overlord.execute(parameters);
	}


	//
	// Class Instances
	//

	@Getter
	private OverlordConfiguration configuration;
	private final ArrayList<ExecutionInstance> instances = new ArrayList<ExecutionInstance>();
	private final NativeLibraryProvider libprovider = new NativeLibraryProvider();


	public void init () throws OverlordException {
		OverlordSystemSupport systemsupport = AbstractOverlordSystemSupport.getSystemSupport();

		URL configfile = OverlordUtilities.findResource(CONFIGURATION_FILE);
		if (configfile == null)
			throw new NoOverlordConfigurationException(CONFIGURATION_FILE);

		InputStream configstream = OverlordUtilities.findResourceAsStream(CONFIGURATION_FILE);

		configuration = new OverlordConfiguration(this);
		configuration.setJavaExecutable(systemsupport.getJavaExecutable());

		XMLConfigurationParser configparser = new XMLConfigurationParser(configfile, configstream, configuration);
		configparser.parse();
	}


	public void execute (String[] parameters) throws OverlordException, IOException, InterruptedException {
		List<ExecutionGroup> groups = new ArrayList<ExecutionGroup>();

		for (String groupname : parameters) {
			ExecutionGroup group;
			if (groupname.matches("t:.*")) {
				String targetName = groupname.substring(2);

				ExecutionTarget target = configuration.getExecutionTarget(targetName);
				if (target == null)
					throw new UnknownExecutionTargetException(targetName, null);

				ExecutionSpecification spec = new ExecutionSpecification(configuration, target);
				spec.setInstances(1);

				group = new ExecutionGroup("target_" + targetName);
				group.addExecutionSpecification(spec);
			} else {
				group = configuration.getExecutionGroup(groupname);
			}

			if (group != null)
				groups.add(group);
			else {
				logger.error("Unknown Execution Group: {}", group);
				return;
			}
		}

		for (ExecutionGroup group : groups)
			group.execute();
	}


	public Collection<ExecutionInstance> getExecutionInstances () {
		return instances;
	}


	public NativeLibraryProvider getNativeLibraryProvider () {
		return libprovider;
	}


	public void addShutdownHooks () {
		Runtime.getRuntime().addShutdownHook(new OverlordDynamicShutdownHook(this));
	}


	public void addExecutionInstance (Thread thread, ExecutionMonitor monitor) {
		instances.add(new ExecutionInstance(thread, monitor));
	}


	public void killInstances () {
		for (ExecutionInstance instance : instances)
			if (instance.getThread().isAlive()) {
				instance.getThread().interrupt();
				instance.getMonitor().killProcess();
			}
	}
}

class ExecutionInstance {
	private final Thread thread;
	private final ExecutionMonitor monitor;


	public ExecutionInstance (Thread thread, ExecutionMonitor monitor) {
		this.thread = thread;
		this.monitor = monitor;
	}


	public Thread getThread () {
		return thread;
	}


	public ExecutionMonitor getMonitor () {
		return monitor;
	}
}
