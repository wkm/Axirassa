
package axirassa.overlord;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashSet;

import lombok.extern.slf4j.Slf4j;

/**
 * {@link NativeLibraryProvider} extracts native libraries from JAR files into a
 * temporary directory where they may be directly accessed by the operating
 * system and loaded via {@link System.loadLibrary()}.
 * 
 * Note that libraries are only extracted if they are on the classpath but
 * within a Jar file where they may not be accessed by the OS.
 * 
 * @author wiktor
 * 
 */
@Slf4j
public class NativeLibraryProvider {

	private static final int BUFFER_SIZE = 65536;
	private static final int BYTE_BUFFER_SIZE = 4096;

	private File tempdir;
	private final HashSet<String> availableLibraries = new HashSet<String>();


	private File getTemporaryDirectory () throws IOException {
		if (tempdir == null) {
			File tempfile = File.createTempFile("OverlordNativeLibrary", "");
			tempfile.delete();
			tempfile.mkdir();

			tempdir = tempfile;
		}

		return tempdir;
	}


	/**
	 * @return the path to the temporary directory containing provided libraries
	 *         if necessary, or null if the libraries may be directly accessed
	 *         by the OS.
	 */
	public String getLibraryPath () {
		if (tempdir == null)
			return null;

		return tempdir.getAbsolutePath();
	}


	public void provideLibrary (String name) throws IOException {
		// check if we already provided the library
		if (availableLibraries.contains(name))
			return;

		URL library = getClass().getResource(name);
		if (library == null)
			throw new ExceptionInInitializerError("Cannot find resource on path: " + name);

		File source = new File(library.getPath());

		if (isWithinJar(source)) {
			InputStream instream = getClass().getResourceAsStream(name);
			copyToDirectory(source, instream);
		}

		availableLibraries.add(name);
	}


	private void copyToDirectory (File source, InputStream instream) throws IOException {
		File directory = getTemporaryDirectory();
		File newfile = new File(directory.getPath() + File.separator + source.getName());

		log.info("COPYING {} TO {}", source.getPath(), directory.getPath());

		InputStream in = new BufferedInputStream(instream, BUFFER_SIZE);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(newfile), BUFFER_SIZE);

		byte[] bytebuffer = new byte[BYTE_BUFFER_SIZE];

		int bytesread;
		while ((bytesread = in.read(bytebuffer)) > 0) {
			out.write(bytebuffer, 0, bytesread);
		}

		in.close();
		out.close();
	}


	private boolean isWithinJar (File file) {
		String[] components = file.getPath().split("\\!", 2);
		String filename = components[0];

		return filename.endsWith(".jar");
	}
}
