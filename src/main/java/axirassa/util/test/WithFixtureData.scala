
package axirassa.util.test

import java.util.HashMap
import org.junit.Assert.assertEquals

import java.io.InputStream
import java.util.Map

import zanoccio.javakit.StringUtilities
import axirassa.util.test.exception.XmlFixtureParsingException

object WithFixtureData {
  val NO_FIXTURE_FILE = "<<no fixture file>>"
  val NO_FIXTURE = "<<no fixture with that name>>"
}

class WithFixtureData(val classObject : Class[_] = getClass()) {
  var fixtureMap = new HashMap[String, String]
  loadFixture()

  def loadFixture() {
    val resourceName = makeFixtureResourceName

    val stream = classObject.getResourceAsStream(resourceName)

    if (stream == null)
      return

    val parser = new XmlFixtureParser(stream)
    fixtureMap = parser.parse()
  }

  def makeFixtureResourceName = "/"+classObject.getCanonicalName().replace(".", "/")+"_fixtures.xml"

  def getFixture(name : String) = {
    if (fixtureMap == null)
      WithFixtureData.NO_FIXTURE_FILE
    else {

      val fixture = fixtureMap.get(name)

      if (fixture == null)
        WithFixtureData.NO_FIXTURE
      else 
        fixture
    }
  }

  def assertFixtureEquals(fixture : String, actual : String) {
    assertEquals(getFixture(fixture), StringUtilities.removeLeadingWhitespace(actual))
  }
}
