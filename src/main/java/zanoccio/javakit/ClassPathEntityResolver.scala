
package zanoccio.javakit

import java.io.IOException
import java.io.InputStream

import org.xml.sax.EntityResolver
import org.xml.sax.InputSource
import org.xml.sax.SAXException

class UnknownResourceException(path : String)
  extends Exception("Could not find the resource "+path)

/**
 * Based on http://www.ibm.com/developerworks/xml/library/x-tipentres.html
 *
 * Searches for entities on the classpath (typically a jar file, otherwise the
 * default resolver would find the entity)
 *
 * @author wiktor
 */
class ClassPathEntityResolver extends EntityResolver {

  /**
   * Attempts to resolve the given entity by searching the classpath. Note
   * that entity must be addressed using the <tt>classpath://</tt> protocol.
   */
  override def resolveEntity(publicid : String, systemid : String) : InputSource = {
    if (systemid == null)
      return null

    if (!usesClassPathProtocol(systemid))
      return null

    val path = extractPath(systemid)

    try {
      val inputstream = ClassLoader.getSystemResourceAsStream(path)
      if (inputstream == null)
        throw new UnknownResourceException(path)

      return new InputSource(inputstream)
    } catch {
      case e : Exception =>
        e.printStackTrace()
        return null
    }
  }

  def usesClassPathProtocol(address : String) = {
    val components = splitAddress(address)
    if (components.length < 1)
      false
    else
      components(0) == "classpath"
  }

  private def splitAddress(address : String) = address.split("://")

  private def extractPath(address : String) = {
    val components = splitAddress(address)
    if (components.length == 2)
      components(1)
    else
      ""
  }
}
