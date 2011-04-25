
package zanoccio.javakit;

/**
 * A ClassLoader which replaces requests to {@link java.lang.System} with
 * requests to {@link LibraryUnpackingSystem}. Quite naughty.
 * 
 * @author wiktor
 */
public class LibraryUnpackingClassLoader extends ClassLoader {
	@Override
	protected synchronized Class loadClass (String className, boolean resolve) throws ClassNotFoundException {
		if (className.equals("java.lang.System")) {
			System.out.println("INTERCEPTING, REPLACING WITH LibraryUnpackingSystem CLASS");
			return super.loadClass("zanoccio.javakit.LibraryUnpackingSystem", resolve);
		} else
			return super.loadClass(className, resolve);
	}
}
