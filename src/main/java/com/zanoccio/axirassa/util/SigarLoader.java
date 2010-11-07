
package com.zanoccio.axirassa.util;

import java.io.IOException;

import com.zanoccio.axirassa.overlord.exceptions.OverlordException;
import com.zanoccio.axirassa.overlord.os.OverlordSystemSupport;
import com.zanoccio.axirassa.overlord.os.AbstractOverlordSystemSupport;

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
		} catch (OverlordException e) {
			throw new ExceptionInInitializerError(e);
		}

		loaded = true;
	}


	public void loadLibrary() throws IOException, OverlordException {
		OverlordSystemSupport systemsupport = AbstractOverlordSystemSupport.getSystemSupport();
		
		switch(systemsupport.getSystem()) {
		case WINDOWS:
			JavaLibraryPath.addFile("/sigar-x86-winnt.dll");
			JavaLibraryPath.addFile("/sigar-amd64-winnt.dll");
			break;
			
		case LINUX:
			JavaLibraryPath.addFile("/libsigar-amd64-linux.so");
			JavaLibraryPath.addFile("/libsigar-x86-linux.so");
			JavaLibraryPath.addFile("/libsigar-ia64-linux.so");
			break;
		}
	}
}
