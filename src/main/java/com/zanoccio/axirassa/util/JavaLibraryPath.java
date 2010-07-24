
package com.zanoccio.axirassa.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Properties;

public class JavaLibraryPath {
	public static void add(File path) throws Exception {
		String newpath = System.getProperty("java.library.path");
		if (newpath == null || newpath.length() < 1)
			newpath = path.getCanonicalPath();
		else
			newpath += File.pathSeparator + path.getCanonicalPath();

		Field f = System.class.getDeclaredField("props");
		f.setAccessible(true);
		Properties props = (Properties) f.get(null);
		props.put("java.library.path", newpath);

		Field loaderpathsfield = ClassLoader.class.getDeclaredField("usr_paths");
		loaderpathsfield.setAccessible(true);
		String[] usrpaths = (String[]) loaderpathsfield.get(null);
		String[] newusrpaths = new String[usrpaths == null ? 1 : usrpaths.length + 1];

		if (usrpaths != null)
			System.arraycopy(usrpaths, 0, newusrpaths, 0, usrpaths.length);

		newusrpaths[newusrpaths.length - 1] = path.getAbsolutePath();
		loaderpathsfield.set(null, newusrpaths);

	}
}
