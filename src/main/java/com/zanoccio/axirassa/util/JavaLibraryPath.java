
package com.zanoccio.axirassa.util;

import java.lang.reflect.Field;

public class JavaLibraryPath {
	public static void add(String path) {
		try {

			Class<? extends Object> clazz = ClassLoader.class;

			Field field = clazz.getDeclaredField("sys_paths");
			boolean accessible = field.isAccessible();
			if (!accessible)
				field.setAccessible(true);

			// remove the current value
			field.set(clazz, null);
			System.setProperty("java.library.path", path);
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}
}
