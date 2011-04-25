
package axirassa.overlord;

import java.util.LinkedHashMap;

import lombok.Getter;
import lombok.Setter;

public class OverlordConfiguration {

	@Getter
	private final OverlordMain overlord;
	
	private final LinkedHashMap<String, ExecutionTarget> targets = new LinkedHashMap<String, ExecutionTarget>();
	private final LinkedHashMap<String, ExecutionGroup> groups = new LinkedHashMap<String, ExecutionGroup>();

	@Getter
	@Setter
	private String baseDirectory;
	
	@Getter
	@Setter
	private String classPath;
	
	@Getter
	@Setter
	private String javaExecutable;


	public OverlordConfiguration (OverlordMain overlord) {
		this.overlord = overlord;
	}



	public void addExecutionGroup (ExecutionGroup group) {
		groups.put(group.getCanonicalName(), group);
	}


	public boolean hasExecutionGroup (String name) {
		return getExecutionGroup(name) != null;
	}


	public ExecutionGroup getExecutionGroup (String name) {
		return groups.get(ExecutionGroup.canonicalizeName(name));
	}


	public void addExecutionTarget (ExecutionTarget target) {
		targets.put(target.getCanonicalName(), target);
	}


	public boolean hasExecutionTarget (String name) {
		return getExecutionTarget(name) != null;
	}


	public ExecutionTarget getExecutionTarget (String name) {
		return targets.get(ExecutionTarget.canonicalizeName(name));
	}

}
