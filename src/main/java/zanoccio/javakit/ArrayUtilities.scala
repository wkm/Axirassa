
package zanoccio.javakit

object ArrayUtilities {
  def toString(array : Array[Byte]) = {
    val sb = new StringBuilder()

    for (i <- 0 until array.length) {
      if (i > 0)
        sb.append('-')

      if (array(i) < 0)
        sb.append(127 + -array(i))
      else
        sb.append(array(i))
    }

    sb.toString()
  }
}
