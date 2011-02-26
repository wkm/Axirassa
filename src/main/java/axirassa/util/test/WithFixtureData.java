
package axirassa.util.test;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.Map;

import axirassa.util.test.exception.XmlFixtureParsingException;

public class WithFixtureData {

	public final static String NO_FIXTURE_FILE = "<<no fixture file>>";
	public final static String NO_FIXTURE = "<<no fixture with that name>>";

	private final Class<? extends Object> classObject;

	private Map<String, String> fixtureMap;


	public WithFixtureData() {
		try {
			this.classObject = getClass();
			loadFixture();
		} catch (XmlFixtureParsingException e) {
			throw new ExceptionInInitializerError(e);
		}
	}


	public WithFixtureData(Class<? extends Object> classObject) throws XmlFixtureParsingException {
		this.classObject = classObject;
		loadFixture();
	}


	public void loadFixture() throws XmlFixtureParsingException {
		String resourceName = makeFixtureResourceName();

		InputStream stream = classObject.getResourceAsStream(resourceName);

		if (stream == null)
			return;

		XmlFixtureParser parser = new XmlFixtureParser(stream);
		fixtureMap = parser.parse();
	}


	private String makeFixtureResourceName() {
		return "/" + classObject.getCanonicalName().replace(".", "/") + "_fixtures.xml";
	}


	public String getFixture(String name) {
		if (fixtureMap == null)
			return NO_FIXTURE_FILE;

		String fixture = fixtureMap.get(name);

		if (fixture == null)
			return NO_FIXTURE;

		return fixture;
	}


	public void assertFixtureEquals(String fixture, String actual) {
		assertEquals(getFixture(fixture), actual);
	}
}
