
package zanoccio.javakit;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * A ClassLoader which provides it's own #findLibrary routine (by extracting
 * libraries from JAR files)
 * 
 * @author wiktor
 */
public class LibraryUnpackingClassLoader extends ClassLoader {

	private static class FindClassClassLoader extends ClassLoader {
		public FindClassClassLoader (ClassLoader parent) {
			super(parent);
		}


		@Override
		public Class<?> findClass (String name) throws ClassNotFoundException {
			return super.findClass(name);
		}
	}





	private static class ChildUrlClassLoader extends URLClassLoader {
		private FindClassClassLoader realParent;


		public ChildUrlClassLoader (URL[] urls, FindClassClassLoader realParent) {
			super(urls, null);
			this.realParent = realParent;
		}


		@Override
		public Class<?> findClass (String name) throws ClassNotFoundException {
			System.out.println("Finding class: " + name);
			try {
				return super.findClass(name);
			} catch (ClassNotFoundException e) {
				return realParent.loadClass(name);
			}
		}
	}


	private ChildUrlClassLoader childClassLoader;


	public LibraryUnpackingClassLoader () {
		super(Thread.currentThread().getContextClassLoader());
		childClassLoader = new ChildUrlClassLoader(new URL[] {}, new FindClassClassLoader(this.getParent()));
	}


	@Override
	protected Class<?> loadClass (String name, boolean resolve) throws ClassNotFoundException {
		try {
			return childClassLoader.findClass(name);
		} catch (ClassNotFoundException e) {
			return super.loadClass(name, resolve);
		}
	}
}
