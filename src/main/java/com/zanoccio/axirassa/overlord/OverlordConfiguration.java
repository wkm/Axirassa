
package com.zanoccio.axirassa.overlord;

import java.util.LinkedHashMap;

public class OverlordConfiguration {

	private final LinkedHashMap<String, ExecutionTarget> targets = new LinkedHashMap<String, ExecutionTarget>();
	private final LinkedHashMap<String, ExecutionGroup> groups = new LinkedHashMap<String, ExecutionGroup>();

	private String basedirectory;
	private String classpath;
	private String javaexecutable;


	public void setBaseDirectory(String basediretory) {
		this.basedirectory = basediretory;
	}


	public void setClassPath(String classpath) {
		this.classpath = classpath;
	}


	public void setJavaExecutable(String javaexecutable) {
		this.javaexecutable = javaexecutable;
	}


	public String getJavaExecutable() {
		return javaexecutable;
	}


	public String getClassPath() {
		return classpath;
	}


	public String getBaseDirectory() {
		return basedirectory;
	}


	public void addExecutionGroup(ExecutionGroup group) {
		groups.put(group.getCanonicalName(), group);
	}


	public boolean hasExecutionGroup(String name) {
		return getExecutionGroup(name) != null;
	}


	public ExecutionGroup getExecutionGroup(String name) {
		return groups.get(ExecutionGroup.canonicalizeName(name));
	}


	public void addExecutionTarget(ExecutionTarget target) {
		targets.put(target.getCanonicalName(), target);
	}


	public boolean hasExecutionTarget(String name) {
		return getExecutionTarget(name) != null;
	}


	public ExecutionTarget getExecutionTarget(String name) {
		return targets.get(ExecutionTarget.canonicalizeName(name));
	}

}
