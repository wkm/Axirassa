
package zanoccio.javakit;

import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * A ClassLoader which provides it's own #findLibrary routine (by extracting
 * libraries from JAR files)
 * 
 * @author wiktor
 */
public class LibraryUnpackingClassLoader extends ClassLoader {

	private String jarFileName;


	public LibraryUnpackingClassLoader (String jarFileName) throws IOException {
		this.jarFileName = jarFileName;

		init();
	}


	private void init () throws IOException {
		System.out.println("SEARCHING JAR FILE: " + jarFileName + " FOR LIBRARY JARS");
		JarFile jarFile = new JarFile(jarFileName);

		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			System.out.println("entry: " + entry);
		}
	}
}
