
package zanoccio.javakit;

public class LibraryUnpackingSystem {
	static {
		try {
			Class systemClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.System");

		} catch (ClassNotFoundException e) {
			e.printStackTrace(System.err);
		}
	}
}
