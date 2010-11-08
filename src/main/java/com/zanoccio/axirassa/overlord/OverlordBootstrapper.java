
package com.zanoccio.axirassa.overlord;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Lightweight wrapper for loading other classes using Overlord's class loader.
 * 
 * @author wiktor
 */
public class OverlordBootstrapper {
	public static void main(String[] parameters) throws ClassNotFoundException, SecurityException,
	        NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		OverlordClassLoader loader = new OverlordClassLoader();

		Thread.currentThread().setContextClassLoader(loader);

		if (parameters.length < 1) {
			System.err.println("Invalid command line.");
			System.exit(-1);
		}

		String classname = parameters[0];
		String[] classparameters = Arrays.copyOfRange(parameters, 1, parameters.length);

		Class<?> classobject = loader.loadClass(classname);
		System.out.println("CLASSLOADER: " + classobject.getClassLoader());
		Method mainmethod = classobject.getMethod("main", String[].class);

		mainmethod.invoke(null, (Object) classparameters);
	}
}
