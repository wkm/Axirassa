
package axirassa.util;

import java.io.File;
import java.net.URL;

import org.jnetpcap.Pcap;

/**
 * Finds the JNI library for jnetpcap on the classpath and adds it to the
 * JavaLibraryPath.
 * 
 * @author wiktor
 */
public class PcapLoader {
	private static boolean loaded = false;


	public static void require() {
		if (!loaded)
			load();
	}


	public static void load() {
		PcapLoader loader = new PcapLoader();
		loader.loadLibrary();
		loaded = true;
	}


	public void loadLibrary() {
		URL library = getClass().getResource("/jnetpcap.dll");
		if (library == null)
			throw new ExceptionInInitializerError("Cannot find jnetpcap library on classpath");

		File path = new File(library.getPath());
		JavaLibraryPath.add(path.getParent());
		System.out.println("PCAP version: " + Pcap.libVersion());
	}
}
