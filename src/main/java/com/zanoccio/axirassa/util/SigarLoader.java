
package com.zanoccio.axirassa.util;

import java.io.File;
import java.net.URL;

/**
 * Similar to {@link PcapLoader} but for Sigar.
 * 
 * @author wiktor
 * 
 */
public class SigarLoader {

	private static boolean loaded = false;


	public static void require() {
		if (!loaded)
			load();
	}


	public static void load() {
		SigarLoader loader = new SigarLoader();
		loader.loadLibrary();
		loaded = true;
	}


	public void loadLibrary() {
		URL library = getClass().getResource("/sigar-x86-winnt.dll");
		if (library == null)
			throw new ExceptionInInitializerError("Cannot fine sigar library");

		File path = new File(library.getPath());
		JavaLibraryPath.add(path.getParent());
	}

}
