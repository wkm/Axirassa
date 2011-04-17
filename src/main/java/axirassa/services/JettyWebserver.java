
package axirassa.services;

import java.io.InputStream;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.xml.XmlConfiguration;

public class JettyWebserver implements Service {
	public static void main (String[] args) throws Exception {
		Service service = new JettyWebserver();
		service.execute();
	}


	@Override
	public void execute () throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream jettyConfig = classLoader.getResourceAsStream("jetty.xml");

		System.out.println("JETTY CONFIG: " + jettyConfig);

		Server server = new Server();
		XmlConfiguration configuration = new XmlConfiguration(jettyConfig);
		configuration.configure(server);
		server.start();
	}
}
