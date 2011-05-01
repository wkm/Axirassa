
package axirassa.util

import java.io.Serializable
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.Security

import org.bouncycastle.jce.provider.BouncyCastleProvider

object MessageDigestProvider {
  private val serialVersionUID = -6652194924404625896L
  private var initialized = false

  def salt = Long.toBinaryString(serialVersionUID).getBytes

  def generate() = {
    initialize()
    MessageDigest.getInstance("SHA512")
  }

  def initialize() {
    if (initialized)
      return

    Security.addProvider(new BouncyCastleProvider())
    initialized = true
  }

}
