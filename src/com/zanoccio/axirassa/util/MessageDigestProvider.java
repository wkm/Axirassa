
package com.zanoccio.axirassa.util;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.zanoccio.axirassa.webapp.exceptions.ExceptionInActionError;

/**
 * Gentle abstraction around MessageDigest that automatically picks the SHA512
 * algorithm and declares the "Bouncy Castle" security Provider.
 * 
 * @author wiktor
 * 
 */
public final class MessageDigestProvider implements Serializable {
	private static final long serialVersionUID = -6652194924404625896L;
	private static boolean initialized = false;


	/**
	 * @return an array of bytes to use as an application-specific part of
	 *         salts. The given salt is based on the {@link #serialVersionUID}
	 *         of this {@link #MessageDigestProvider()}
	 */
	public static byte[] salt() {
		return Long.toBinaryString(serialVersionUID).getBytes();
	}


	public static MessageDigest generate() {
		initialize();

		try {
			MessageDigest msgdigest;
			msgdigest = MessageDigest.getInstance("SHA512");
			return msgdigest;
		} catch (NoSuchAlgorithmException e) {
			throw new ExceptionInActionError("Could not generate exception", e);
		}
	}


	public static void initialize() {
		if (initialized)
			return;

		Security.addProvider(new BouncyCastleProvider());
		initialized = true;
	}

}
