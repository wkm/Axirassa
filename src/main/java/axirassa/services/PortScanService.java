
package axirassa.services;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class PortScanService implements Service {

	public static final int THREADS = 5;
	public static final int LO_PORT = 0;
	public static final int HI_PORT = 10; // 65536

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
		InetAddress address = InetAddress.getByName(this.address);
		threads = new Thread[THREADS];

		for (int port = LO_PORT; port < HI_PORT; port++)
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
			// Socket socket = new Socket();

			// try {
			// socket.setReuseAddress(true);
			// socket.setSoLinger(false, 1);
			// socket.setPerformancePreferences(2, 1, 0);
			// socket.setTcpNoDelay(true);
			// socket.setKeepAlive(false);
			// socket.bind(null);
			// } catch (ConnectException e) {
			// try {
			// socket.close();
			// } catch (IOException e1) {
			// }
			// } catch (SocketException e) {
			// e.printStackTrace();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }

			int port;

			while (true) {
				synchronized (portqueue) {
					if (portqueue.isEmpty())
						return;
					else
						port = portqueue.removeFirst();
				}

				try {
					Socket socket = new Socket();
					socket.connect(new InetSocketAddress(address, port), 4000);
					socket.close();

					synchronized (openports) {
						openports.add(port);
					}
				} catch (SocketTimeoutException e) {
				} catch (IOException e) {
				}
			}
		}
	}
}
