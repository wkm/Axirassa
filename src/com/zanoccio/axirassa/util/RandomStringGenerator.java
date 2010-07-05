
package com.zanoccio.axirassa.util;

import java.math.BigInteger;
import java.security.SecureRandom;

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


	public String randomString(int length) {
		int bits = length * 5;
		return new BigInteger(bits, random).toString(32);
	}

}
