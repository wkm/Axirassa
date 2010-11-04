
package com.zanoccio.axirassa.util;

import java.io.IOException;

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
		try {
			loader.loadLibrary();
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		}

		loaded = true;
	}


	public void loadLibrary() throws IOException {
		JavaLibraryPath.addFile("/sigar-x86-winnt.dll");
	}
}
