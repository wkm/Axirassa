
package axirassa.util

import java.security.MessageDigest
import java.security.Security

import java.lang.Long.toBinaryString

import org.bouncycastle.jce.provider.BouncyCastleProvider

object MessageDigestProvider {
  private val serialVersionUID = -6652194924404625896L
  private var initialized = false

  def salt = toBinaryString(serialVersionUID).getBytes

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
