
package axirassa.util

import java.util.Collection
import java.lang.Math.log10
import java.lang.Math.pow

import org.apache.tapestry5.json.JSONLiteral

class JSONConstructor {
  val BUFFER_SIZE = 1024 * 1024

  /**
   * TODO (BugzID:83)
   */
  def format(value : Double, precision : Int, granularity : Double) : String = {
    if (value < granularity)
      return "0"

    // one less than the number of digits
    val magnitude = log10(value).asInstanceOf[Int]

    // apply the granularity
    val valueWithGranularity = (value * granularity).asInstanceOf[Long] / granularity

    // compute the amount of extra characters
    var extra = 0
    if (valueWithGranularity % 1 > 0)
      extra = 2
    else
      extra = 1

    var cursor = precision + extra
    val result = new Array[Char](precision + extra)

    // from the end, copy the result
    val start = pow(10, magnitude - precision)
    while (cursor >= 0) {
      cursor -= 1
    }

    return new String(result)
  }

  def generate(array : Array[Array[Array[Double]]]) = {
    val sb = new StringBuilder(BUFFER_SIZE)

    sb.append('[');
    for (i <- 0 until array.length) {
      sb.append('[');
      for (j <- 0 until array(i).length) {
        sb.append('[');
        for (k <- 0 until array(i)(j).length) {
          sb.append(array(i)(j)(k));

          if (k < array(i)(j).length - 1)
            sb.append(',');
        }
        sb.append(']');

        if (j < array(i).length - 1)
          sb.append(',');
      }
      sb.append(']');

      if (i < array.length - 1)
        sb.append(',');
    }
    sb.append(']');

    new JSONLiteral(sb.toString())
  }

  def generate[T](data : Collection[T]) = {
    val sb = new StringBuilder(BUFFER_SIZE)
    val iter = data.iterator()

    sb.append('[')
    while (iter.hasNext()) {
      sb.append(iter.next())

      if (iter.hasNext())
        sb.append(',')
    }
    sb.append(']')

    new JSONLiteral(sb.toString())
  }

  def generateStrings(data : Collection[String]) = {
    val sb = new StringBuilder(BUFFER_SIZE)
    val iter = data.iterator()

    sb.append('[')
    while (iter.hasNext()) {
      var v = iter.next()
      v = v.replace("\\", "\\\\")
      v = v.replace("'", "\\")

      sb.append('\'')
      sb.append(v)
      sb.append('\'')

      if (iter.hasNext())
        sb.append(',')
    }
    sb.append(']')

    new JSONLiteral(sb.toString())
  }
}
