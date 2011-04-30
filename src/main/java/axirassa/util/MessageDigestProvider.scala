
package axirassa.util

import java.io.Serializable
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.Security

import org.bouncycastle.jce.provider.BouncyCastleProvider

object MessageDigestProvider {
	private val serialVersionUID = -6652194924404625896L
	
	def salt = Long.toBinaryString(serialVersionUID).getBytes
	
	def generate() = {
	  initialize()
	  MessageDigest.getInstance("SHA512")
	}
	
	def initialize() = {
	  Security.addProvider(new BouncyCastleProvider())
	}
	
	public static void initialize() {
		if (initialized)
			return

		Security.addProvider(new BouncyCastleProvider())
		initialized = true
	}

}
