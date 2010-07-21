
package com.zanoccio.axirassa.util;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class MessageDigestProvider implements Serializable {
	private static final long serialVersionUID = -6652194924404625896L;
	private static boolean initialized = false;


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
			throw new ExceptionInInitializerError(e);
		}
	}


	public static void initialize() {
		if (initialized)
			return;

		Security.addProvider(new BouncyCastleProvider());
		initialized = true;
	}

}
