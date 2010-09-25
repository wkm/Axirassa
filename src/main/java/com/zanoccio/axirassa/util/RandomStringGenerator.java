
package com.zanoccio.axirassa.util;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Lightweight singleton class for generating random strings of a given length;
 * powered by {@link SecureRandom}.
 * 
 * @author wiktor
 * 
 */
public class RandomStringGenerator {
	public static RandomStringGenerator getInstance() {
		if (instance == null)
			instance = new RandomStringGenerator();
		return instance;
	}


	private static RandomStringGenerator instance;
	private final SecureRandom random = new SecureRandom();


	private RandomStringGenerator() {
	}


	public static String makeRandomString(int length) {
		return getInstance().randomString(length);
	}


	/**
	 * @return a string of the given length containing a base-32 encoded random
	 *         number.
	 */
	public String randomString(int length) {
		int bits = length * 5;
		return new BigInteger(bits, random).toString(32);
	}
}
