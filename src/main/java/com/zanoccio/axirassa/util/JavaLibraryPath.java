
package com.zanoccio.axirassa.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URL;

/**
 * Convenience class for rewriting the "java.library.path" system setting
 * 
 * @author wiktor
 * 
 */
public class JavaLibraryPath {

	private static final int BUFFER_SIZE = 65536;


	/**
	 * adds a file to the path (generally the file's directory).
	 * 
	 * Includes special handling of .dll and .so files by copying them out of a
	 * containing .jar (if applicable) into a temporary directory and then
	 * adding them to the classpath.
	 */
	public static void addFile(String path) throws IOException {
		Object obj = "raw object";
		URL library = obj.getClass().getResource(path);
		if (library == null)
			throw new ExceptionInInitializerError("Cannot find resource on path: " + path);

		File file = new File(library.getPath());

		if (isLibraryFile(file)) {
			InputStream instream = obj.getClass().getResourceAsStream(path);
			file = copyToTemporaryDirectory(file, instream);

			System.out.println("Extracting dynamic library " + path + " to: " + file.getPath());
		}

		add(file.getParent());
	}


	private static boolean isLibraryFile(File file) {
		String[] components = file.getName().split("\\.");
		String suffix = components[components.length - 1];

		if (suffix.equalsIgnoreCase("dll"))
			return true;
		else if (suffix.equalsIgnoreCase("so"))
			return true;
		else
			return false;
	}


	private static File copyToTemporaryDirectory(File source, InputStream stream) throws IOException {
		File directory = createTemporaryDirectory();
		File newfile = new File(directory.getPath() + File.separator + source.getName());

		InputStream in = new BufferedInputStream(stream, BUFFER_SIZE);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(newfile), BUFFER_SIZE);

		int cursor;
		while ((cursor = in.read()) != -1)
			out.write(cursor);

		in.close();
		out.close();

		return newfile;
	}


	private static File createTemporaryDirectory() throws IOException {
		File tempfile = File.createTempFile("javalibrarypath", "");
		tempfile.delete();
		tempfile.mkdir();

		return tempfile;
	}


	public static void add(String path) {
		try {
			Class<? extends Object> classobject = ClassLoader.class;

			Field syspathsfield = classobject.getDeclaredField("sys_paths");
			Field userpathsfield = classobject.getDeclaredField("usr_paths");
			
			if (!syspathsfield.isAccessible())
				syspathsfield.setAccessible(true);
			
			if(!userpathsfield.isAccessible())
				userpathsfield.setAccessible(true);
			

			String current = System.getProperty("java.library.path");
			path = path + ";" + current;

			System.out.println("Adjusting path to: " + path);

			// remove the current value
			syspathsfield.set(classobject, null);
			userpathsfield.set(classobject, null);
			
			System.setProperty("java.library.path", path);
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}
}
