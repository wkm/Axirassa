
package com.zanoccio.axirassa.overlord;

public enum XMLName {
	// elements
	TARGET,
	GROUP,
	EXECUTE,

	// attributes
	INSTANCES,
	NAME,
	JVMOPTIONS,
	CLASS;

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}
