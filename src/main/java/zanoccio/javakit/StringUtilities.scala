
package zanoccio.javakit

import java.io.InputStream
import java.util.regex.Pattern
import io.Source


object StringUtilities {
  /**
   * Read in an entire InputStream as a UTF-8 encoded String.
   *
   * @throws IOException
   */
  def stringFromStream(is : InputStream, bufferSize : Int = 4096) : String = {
    Source.fromInputStream(is, "UTF-8").mkString
  }

  def removeLeadingWhitespace(content : String) = {
    val pattern = Pattern.compile("(?m)^([ \t]*)(.*)$")
    val matcher = pattern.matcher(content)

    val stripped = new StringBuilder()

    while (matcher.find()) {
      stripped.append(matcher.group(2))
      stripped.append('\n')
    }

    stripped.toString().trim()
  }
}
