
package com.zanoccio.axirassa.overlord;

public class OverlordClassLoader extends ClassLoader {

	public OverlordClassLoader() {
		super(OverlordClassLoader.class.getClassLoader());
	}


	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> object = super.loadClass(name);
		System.out.println("##### LOADING CLASS " + name);
		return object;
	}


	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		System.out.println("##### FINDING CLASS " + name);
		throw new ClassNotFoundException(name);
	}


	@Override
	public String findLibrary(String name) {
		System.out.println("##### LOADING LIBRARY " + name);
		return null;
	}

}
