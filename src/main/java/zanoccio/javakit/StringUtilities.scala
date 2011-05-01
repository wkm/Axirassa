
package zanoccio.javakit

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.util.regex.Matcher
import java.util.regex.Pattern

object StringUtilities {
  /**
   * Read in an entire InputStream as a UTF-8 encoded String.
   *
   * @throws IOException
   */
  def stringFromStream(is : InputStream, bufferSize : Int = 4096) : String = {
    if (is == null)
      return ""

    val sb = new StringBuffer()
    val buffer = new Array[Char](bufferSize)

    try {
      val reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), bufferSize)
      var bytesRead = 0
      while ((bytesRead = reader.read(buffer)) != -1) {
        sb.append(buffer, 0, bytesRead)
      }
    } finally {
      is.close()
    }

    return sb.toString()
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
