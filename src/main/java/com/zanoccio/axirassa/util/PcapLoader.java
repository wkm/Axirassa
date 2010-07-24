
package com.zanoccio.axirassa.util;

import java.io.File;
import java.net.URL;

import org.jnetpcap.Pcap;

public class PcapLoader {
	public static void load() {
		PcapLoader loader = new PcapLoader();
		loader.loadLibrary();
	}


	public void loadLibrary() {
		URL library = getClass().getResource("/jnetpcap.dll");
		if (library == null)
			throw new ExceptionInInitializerError("Cannot find jnetpcap library on classpath");

		File path = new File(library.getPath());

		System.out.println("file: " + path.getParent());

		// System.load("C:/Users/wiktor/Code/X/src/main/resources/jnetpcap.dll");
		JavaLibraryPath.add(path.getParent());
		System.out.println("PCAP version: " + Pcap.libVersion());
	}
}
