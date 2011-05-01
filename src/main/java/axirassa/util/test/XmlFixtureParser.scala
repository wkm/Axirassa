
package axirassa.util.test

import zanoccio.javakit.StringUtilities
import java.io.InputStream
import java.util.HashMap
import java.util.Map

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList

import zanoccio.javakit.ClassPathEntityResolver
import zanoccio.javakit.StringUtilities
import axirassa.overlord.IterableNodeList

class XmlFixtureParsingException(e : Exception)
  extends Exception(e)

class XmlFixtureParser(inputStream : InputStream) {
  var dom : Document = _
  var docroot : Element = _

  val fixtures = new HashMap[String, String]

  def parse() = {
    val dbf = DocumentBuilderFactory.newInstance()

    try {
      val db = dbf.newDocumentBuilder()
      db.setEntityResolver(new ClassPathEntityResolver())
      dom = db.parse(inputStream)
    } catch {
      case e : Exception => throw new XmlFixtureParsingException(e)
    }

    docroot = dom.getDocumentElement()

    fixtures.clear()
    createFixtures()

    fixtures
  }

  private def createFixtures() {
    val fixtureList = docroot.getElementsByTagName(FixtureXmlName.FIXTURE.toString())
    for (fixtureNode <- new IterableNodeList(fixtureList))
      createFixture(fixtureNode)
  }

  private def createFixture(fixtureNode : Node) {
    val attributes = fixtureNode.getAttributes()
    val nameAttribute = attributes.getNamedItem(FixtureXmlName.NAME.toString())

    val name = nameAttribute.getTextContent()

    var content = fixtureNode.getTextContent()
    content = StringUtilities.removeLeadingWhitespace(content).trim()

    fixtures.put(name, content)
  }

}
