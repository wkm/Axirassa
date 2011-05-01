
package axirassa.util

import java.security.SecureRandom

/**
 * Lightweight singleton class for generating random strings of a given length
 * powered by {@link SecureRandom}.
 *
 * @author wiktor
 *
 */
object RandomStringGenerator {
  private var generatorInstance : RandomStringGenerator = null
  def instance = {
    if (generatorInstance == null)
      generatorInstance = new RandomStringGenerator

    generatorInstance
  }

  def makeRandomString(length : Int) =
    instance.randomString(length)

  def makeRandomStringToken(length : Int) =
    instance.randomStringToken(length)
}

class RandomStringGenerator {
  val random = new SecureRandom()

  /**
   * @return a string of the given length containing random bytes (except
   *         0x00)
   */
  def randomString(length : Int) = {
    val buffer = new Array[Byte](length)

    for (i <- 0 until buffer.length)
      if (buffer(i) == 0)
        buffer(i) = randomNonZeroByte

    random.nextBytes(buffer)

    new String(buffer)
  }

  def randomNonZeroByte = (random.nextInt(254) + 1).asInstanceOf[Byte]

  def randomStringToken(length : Int) = {
    val buffer = new Array[Byte](length)
    var index = 0
    while (index < length) {
      var value = random.nextLong()

      if (value < 0)
        value = -value

      val str = java.lang.Long.toString(value, 32)
      val byteString = str.getBytes()

      for (i <- 0 until byteString.length if index < length) {
        buffer(index) = byteString(i)
        index += 1
      }
    }

    new String(buffer)
  }
}
