
package com.zanoccio.axirassa.util;

import org.jnetpcap.Pcap;

public class PCap {
	public static void loadLibrary() throws Exception {
		System.out.println("PCAP version: " + Pcap.libVersion());
	}
}
