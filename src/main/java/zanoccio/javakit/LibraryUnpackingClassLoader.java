
package zanoccio.javakit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;

/**
 * A ClassLoader which provides it's own #findLibrary routine (by extracting
 * libraries from JAR files)
 * 
 * @author wiktor
 */
public class LibraryUnpackingClassLoader extends URLClassLoader {
	private static final int BUFFER_SIZE = 65536;
	private static final int BYTE_BUFFER_SIZE = 8192;

	private final HashSet<String> availableLibraries = new HashSet<String>();
	private String jarFileName;
	private File tempdir;


	public LibraryUnpackingClassLoader (String jarFileName) throws IOException {
		super(new URL[] {}, jarFileName == null ? LibraryUnpackingClassLoader.class.getClassLoader() : null);
		if (jarFileName != null)
			addURL(new URL(jarFileName));

		this.jarFileName = jarFileName;
	}


	@Override
	public String findLibrary (String name) {
		// Can't find a library inside a jar if we don't have a jar.
		if (jarFileName == null)
			return null;

		String rawName = File.separator + formLibraryName(name);
		System.out.println("LIBRARY NAME: " + rawName);

		try {
			URL library = getClass().getResource(rawName);
			if (library == null) {
				System.out.println("Could not find resource: " + library);
				return null;
			}
			InputStream instream = getClass().getResourceAsStream(rawName);

			if (instream == null)
				System.out.println("INPUT STREAM " + instream);

			getTemporaryDirectory();
			File newfile = new File(tempdir.getPath() + rawName);

			InputStream in = new BufferedInputStream(instream);
			OutputStream out = new BufferedOutputStream(new FileOutputStream(newfile));

			byte[] bytebuffer = new byte[BYTE_BUFFER_SIZE];

			int bytesread;
			while ((bytesread = in.read(bytebuffer)) > 0)
				out.write(bytebuffer, 0, bytesread);

			in.close();
			out.close();

			return newfile.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace(System.err);
			return null;
		}
	}


	private void getTemporaryDirectory () throws IOException {
		if (tempdir == null) {
			File tempfile = File.createTempFile("classloader", "");
			tempfile.delete();
			tempfile.mkdir();

			tempdir = tempfile;
		}
	}


	private String formLibraryName (String name) {
		// TODO this is incredibly basic (only for charva)
		String osName = System.getProperty("os.name");
		if (osName.contains("Linux"))
			return "lib" + name + ".so";
		else if (osName.contains("Mac"))
			return "lib" + name + ".jnilib";
		else if (osName.contains("Windows"))
			return name + ".dll";
		else
			return null;
	}
}
