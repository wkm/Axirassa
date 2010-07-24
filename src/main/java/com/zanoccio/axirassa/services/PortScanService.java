
package com.zanoccio.axirassa.services;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class PortScanService implements Service {

	public static int THREADS = 10;

	private String address;
	private final Set<Integer> openports;
	private final LinkedList<Integer> portqueue;
	private Thread[] threads;


	public PortScanService() {
		portqueue = new LinkedList<Integer>();
		openports = new HashSet<Integer>();
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getAddress() {
		return address;
	}


	public Set<Integer> getOpenPorts() {
		return openports;
	}


	@Override
	public void execute() throws UnknownHostException, InterruptedException {
		int max_port = 65536;

		InetAddress address = InetAddress.getByName(this.address);
		threads = new Thread[THREADS];

		for (int port = 0; port < max_port; port++)
			portqueue.add(port);

		// start all the threads
		for (int i = 0; i < THREADS; i++) {
			threads[i] = new PortScanWorker(address);
			threads[i].start();
		}

		// wait for all the threads to finish
		for (int i = 0; i < THREADS; i++) {
			threads[i].join();
		}
	}





	private class PortScanWorker extends Thread {
		private final InetAddress address;


		public PortScanWorker(InetAddress address) {
			this.address = address;
		}


		@Override
		public void run() {
			Socket s = new Socket();
			int port;

			while (true) {
				synchronized (portqueue) {
					if (portqueue.isEmpty())
						return;
					else
						port = portqueue.removeFirst();
				}

				try {
					System.out.println(port + ": started on " + address.getCanonicalHostName());
					s.connect(new InetSocketAddress(address, port), 2000);
					s.close();
					System.out.println(port + ": open");
					synchronized (openports) {
						openports.add(port);
					}
				} catch (IOException e) {
					System.out.println(port + ": closed");
				}
			}
		}
	}
}
