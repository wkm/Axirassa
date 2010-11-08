
package com.zanoccio.axirassa.overlord;

public enum XMLName {
	// elements
	TARGET,
	GROUP,
	EXECUTE,
	JVMOPTION,
	LIBRARY,

	// attributes
	INSTANCES,
	NAME,
	CLASS;

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}
