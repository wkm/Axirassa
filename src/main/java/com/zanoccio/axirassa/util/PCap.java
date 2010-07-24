
package com.zanoccio.axirassa.util;

import java.net.URL;

import org.jnetpcap.Pcap;

public class PCap {
	public static void loadLibrary() {
		URL library = ClassLoader.getSystemResource("jnetpcap.dll");

		System.out.println("file: " + library);
		if (library == null)
			throw new ExceptionInInitializerError("Cannot find jnetpcap library");

		System.load("C:/Users/wiktor/Code/X/src/main/resources/jnetpcap.dll");
		System.out.println("PCAP version: " + Pcap.libVersion());
	}
}
