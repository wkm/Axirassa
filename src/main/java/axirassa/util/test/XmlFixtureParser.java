
package axirassa.util.test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import zanoccio.javakit.ClassPathEntityResolver;
import zanoccio.javakit.StringUtilities;
import axirassa.overlord.IterableNodeList;
import axirassa.util.test.exception.XmlFixtureParsingException;

public class XmlFixtureParser {
	private final InputStream inputStream;
	private Document dom;
	private Element docroot;

	private final HashMap<String, String> fixtures = new HashMap<String, String>();


	public XmlFixtureParser(InputStream inputStream) {
		this.inputStream = inputStream;
	}


	public Map<String, String> parse() throws XmlFixtureParsingException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			db.setEntityResolver(new ClassPathEntityResolver());
			dom = db.parse(inputStream);
		} catch (Exception e) {
			throw new XmlFixtureParsingException(e);
		}

		docroot = dom.getDocumentElement();

		fixtures.clear();
		createFixtures();

		return fixtures;
	}


	private void createFixtures() {
		NodeList fixtureList = docroot.getElementsByTagName(FixtureXmlName.FIXTURE.toString());
		for (Node fixtureNode : new IterableNodeList(fixtureList))
			createFixture(fixtureNode);
	}


	private void createFixture(Node fixtureNode) {
		NamedNodeMap attributes = fixtureNode.getAttributes();
		Node nameAttribute = attributes.getNamedItem(FixtureXmlName.NAME.toString());

		String name = nameAttribute.getTextContent();
		String content = fixtureNode.getTextContent();

		content = StringUtilities.removeLeadingWhitespace(content).trim();

		fixtures.put(name, content);
	}

};
