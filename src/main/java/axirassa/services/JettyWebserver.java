
package axirassa.services;


public class JettyWebserver /* implements Service */{
	/*
	 * private static final Logger log =
	 * LoggerFactory.getLogger(JettyWebserver.class); private static final
	 * String BASE_PATH = "webapp/";
	 * 
	 * 
	 * public static void main (String[] args) throws Exception { Service
	 * service = new JettyWebserver(); service.execute(); }
	 * 
	 * 
	 * @Override public void execute () throws Exception { Server server = new
	 * Server();
	 * 
	 * ClassLoader classLoader = getClass().getClassLoader();
	 * 
	 * InputStream jettyConfig = classLoader.getResourceAsStream("jetty.xml");
	 * XmlConfiguration configuration = new XmlConfiguration(jettyConfig);
	 * configuration.configure(server);
	 * 
	 * URL baseLocation =
	 * getClass().getProtectionDomain().getCodeSource().getLocation();
	 * 
	 * if (baseLocation == null) { log.warn("COULD NOT LOCATE RESOURCE: " +
	 * BASE_PATH); log.warn("Assuming developmental deployment");
	 * 
	 * WebAppContext handler = new WebAppContext();
	 * 
	 * handler.setDescriptor("src/main/webapp/WEB-INF/web.xml");
	 * handler.setResourceBase("src/main/webapp"); handler.setContextPath("/");
	 * handler.setParentLoaderPriority(true); handler.setServer(server);
	 * 
	 * server.setHandler(handler); } else { log.info("Extracting jar file");
	 * String basePath =
	 * extractJarContents("/Users/wiktor/PCode/X/target/axir-distribution.jar");
	 * 
	 * WebAppContext handler = new WebAppContext(); handler.setContextPath("/");
	 * handler.setDescriptor(basePath + "/webapp/WEB-INF/web.xml");
	 * handler.setResourceBase(basePath + "/webapp"); handler.setServer(server);
	 * server.setHandler(handler); }
	 * 
	 * server.start();
	 * 
	 * }
	 * 
	 * 
	 * private String extractJarContents (String jarFile) throws IOException {
	 * File dir = getTemporaryDirectory(); JarFile jar = new JarFile(jarFile);
	 * 
	 * Enumeration<JarEntry> entries = jar.entries(); while
	 * (entries.hasMoreElements()) { JarEntry entry = entries.nextElement();
	 * File outFile = new File(dir, entry.getName());
	 * 
	 * if (entry.isDirectory()) { log.trace("Directory: {}", entry);
	 * outFile.mkdirs(); continue; }
	 * 
	 * log.trace("Extracting {} to {}\n", entry.getName(),
	 * outFile.getAbsolutePath()); try {
	 * 
	 * InputStream in = new BufferedInputStream(jar.getInputStream(entry));
	 * OutputStream out = new BufferedOutputStream(new
	 * FileOutputStream(outFile));
	 * 
	 * byte[] buffer = new byte[4096]; while (true) { int bytesRead =
	 * in.read(buffer); if (bytesRead <= 0) break; out.write(buffer, 0,
	 * bytesRead); }
	 * 
	 * out.flush(); out.close();
	 * 
	 * in.close(); } catch (IOException e) {
	 * log.warn("Exceptions when extracting: {} {}", entry.getName(),
	 * e.getMessage()); } }
	 * 
	 * return dir.getPath(); }
	 * 
	 * 
	 * private File getTemporaryDirectory () throws IOException { File tempfile
	 * = File.createTempFile("axoverlord", ""); tempfile.delete();
	 * tempfile.mkdir();
	 * 
	 * return tempfile; }
	 */
}
