
package zanoccio.javakit;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import zanoccio.javakit.exception.UnknownResourceException;

/**
 * Based on http://www.ibm.com/developerworks/xml/library/x-tipentres.html
 * 
 * Searches for entities on the classpath (typically a jar file, otherwise the
 * default resolver would find the entity)
 * 
 * @author wiktor
 */
public class ClassPathEntityResolver implements EntityResolver {

	/**
	 * Attempts to resolve the given entity by searching the classpath. Note
	 * that entity must be addressed using the <tt>classpath://</tt> protocol.
	 */
	@Override
	public InputSource resolveEntity(String publicid, String systemid) throws SAXException, IOException {
		if (systemid == null)
			return null;

		if (!usesClassPathProtocol(systemid))
			return null;

		String path = extractPath(systemid);

		try {
			InputStream inputstream = ClassLoader.getSystemResourceAsStream(path);

			if (inputstream == null)
				throw new UnknownResourceException(path);

			return new InputSource(inputstream);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	private boolean usesClassPathProtocol(String address) {
		String[] components = splitAddress(address);
		if (components.length < 1)
			return false;

		return components[0].equals("classpath");
	}


	private String[] splitAddress(String address) {
		return address.split("://");
	}


	private String extractPath(String address) {
		String[] components = splitAddress(address);
		if (components.length == 2)
			return components[1];
		else
			return "";
	}
}
