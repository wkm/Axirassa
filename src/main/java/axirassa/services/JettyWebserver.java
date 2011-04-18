
package axirassa.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyWebserver implements Service {
	private static final Logger log = LoggerFactory.getLogger(JettyWebserver.class);
	private static final String BASE_PATH = "WEB-INF/web.xml";


	public static void main (String[] args) throws Exception {
		Service service = new JettyWebserver();
		service.execute();
	}


	@Override
	public void execute () throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream jettyConfig = classLoader.getResourceAsStream("jetty.xml");

		Server server = new Server();
		XmlConfiguration configuration = new XmlConfiguration(jettyConfig);
		configuration.configure(server);

		WebAppContext handler = new WebAppContext();
		URL warUrl = getClass().getClassLoader().getResource(BASE_PATH);

		if (warUrl == null) {
			log.warn("COULD NOT LOCATE RESOURCE: " + BASE_PATH);
			log.warn("Assuming developmental deployment");

			handler.setDescriptor("src/main/webapp/WEB-INF/web.xml");
			handler.setResourceBase("src/main/webapp");
		} else {
			log.info("Extracting jar file");
			String basePath = extractJarContents("/Users/wiktor/PCode/X/target/axir-distribution.jar");
			handler.setDescriptor(basePath + "/WEB-INF/web.xml");
			handler.setResourceBase(basePath + "/");
		}

		log.info("descriptor:    {}", handler.getDescriptor());
		log.info("resource base: {}", handler.getResourceBase());

		handler.setContextPath("/");
		handler.setParentLoaderPriority(true);

		SecurityHandler securityHandler = handler.getSecurityHandler();
		securityHandler.setLoginService(new HashLoginService());

		server.setHandler(handler);

		server.start();

	}


	private String extractJarContents (String jarFile) throws IOException {
		File dir = getTemporaryDirectory();
		JarFile jar = new JarFile(jarFile);

		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			File outFile = new File(dir, entry.getName());

			if (entry.isDirectory()) {
				log.trace("Directory: {}", entry);
				outFile.mkdirs();
				continue;
			}

			log.trace("Extracting {} to {}\n", entry.getName(), outFile.getAbsolutePath());
			try {

				InputStream in = new BufferedInputStream(jar.getInputStream(entry));
				OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));

				byte[] buffer = new byte[4096];
				while (true) {
					int bytesRead = in.read(buffer);
					if (bytesRead <= 0)
						break;
					out.write(buffer, 0, bytesRead);
				}

				out.flush();
				out.close();

				in.close();
			} catch (IOException e) {
				log.warn("Exceptions when extracting: {} {}", entry.getName(), e.getMessage());
			}
		}

		return dir.getPath();
	}


	private File getTemporaryDirectory () throws IOException {
		File tempfile = File.createTempFile("axoverlord", "");
		tempfile.delete();
		tempfile.mkdir();

		return tempfile;
	}
}
