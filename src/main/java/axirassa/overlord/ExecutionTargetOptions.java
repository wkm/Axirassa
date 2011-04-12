
package axirassa.overlord;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import lombok.Getter;

public class ExecutionTargetOptions {

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


	//
	// Class Instances
	//

	private final LinkedHashMap<JVMOption, String> options = new LinkedHashMap<JVMOption, String>();
	
	@Getter
	private final ArrayList<String> libraries = new ArrayList<String>();


	public void addJVMOption(String option, String value) {
		addJVMOption(JVMOption.valueOf(option.toUpperCase()), value);
	}


	public void addJVMOption(JVMOption option, String value) {
		options.put(option, value);
	}


	public void addLibrary(String name) {
		libraries.add(name);
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
	 * @return true if this target requires native libraries
	 */
	public boolean needsLibraries() {
		return libraries.size() > 0;
	}


	/**
	 * @return the number of JVM options
	 */
	public int size() {
		return options.size();
	}
}
