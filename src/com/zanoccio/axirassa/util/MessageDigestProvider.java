
package com.zanoccio.axirassa.util;

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
public final class MessageDigestProvider {

	private static boolean initialized = false;


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
