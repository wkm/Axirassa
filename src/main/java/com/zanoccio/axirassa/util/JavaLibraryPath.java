
package com.zanoccio.axirassa.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
	 * 
	 * @throws IOException
	 */
	public static void addFile(String path) throws IOException {
		Object obj = "raw object";
		URL library = obj.getClass().getResource(path);
		if (library == null)
			throw new ExceptionInInitializerError("Cannot find resource on path: " + path);

		File file = new File(library.getPath());

		System.out.println("File: " + path + " adding to classpath");

		if (isLibraryFile(file))
			file = copyToTemporaryDirectory(file);

		System.out.println("Eventually ading: " + file.getParent() + " to class path");
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


	private static File copyToTemporaryDirectory(File source) throws IOException {
		File directory = createTemporaryDirectory();
		File newfile = new File(directory.getPath() + File.separator + source.getName());

		System.out.println("Copying from " + source.getPath() + " --> " + newfile.getPath());

		InputStream in = new BufferedInputStream(new FileInputStream(source), BUFFER_SIZE);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(newfile), BUFFER_SIZE);

		int cursor;
		while ((cursor = in.read()) != -1)
			out.write(cursor);

		in.close();
		out.close();

		System.out.println("Done.");

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

			Field field = classobject.getDeclaredField("sys_paths");
			boolean accessible = field.isAccessible();
			if (!accessible)
				field.setAccessible(true);

			String current = System.getProperty("java.library.path");
			path = path + ";" + current;

			System.out.println("Adjusting path to: " + path);

			// remove the current value
			field.set(classobject, null);
			System.setProperty("java.library.path", path);
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}
}
