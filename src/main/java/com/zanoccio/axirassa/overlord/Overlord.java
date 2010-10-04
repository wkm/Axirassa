
package com.zanoccio.axirassa.overlord;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.zanoccio.axirassa.overlord.exceptions.NoOverlordConfigurationException;
import com.zanoccio.axirassa.overlord.exceptions.OverlordException;

/**
 * A process starting/monitoring daemon and framework.
 * 
 * AxOverlord hooks into a HornetQ message passing framework
 * 
 * @author wiktor
 * 
 */
public class Overlord {
	private static final String CONFIGURATION_FILE = "axoverlord.cfg.xml";


	public static void main(String[] parameters) throws OverlordException {
		Overlord overlord = new Overlord();
		overlord.execute();
	}


	private Document dom;


	public void execute() throws OverlordException {
		// locate configuration file
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		URL configfile = ClassLoader.getSystemResource(CONFIGURATION_FILE);
		if (configfile == null)
			throw new NoOverlordConfigurationException(CONFIGURATION_FILE);

		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(configfile.getPath());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Element docroot = dom.getDocumentElement();
		NodeList nodelist = docroot.getElementsByTagName("group");
		for (int i = 0; i < nodelist.getLength(); i++) {
			Node node = nodelist.item(i);
			System.out.println("attr: " + node.getAttributes());

		}
	}
}
