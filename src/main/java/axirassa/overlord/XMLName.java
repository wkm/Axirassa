
package axirassa.overlord;

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
	CLASS,
	VALUE,
	INITIALDELAY,
	AUTORESTART;

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}
