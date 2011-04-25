
package axirassa.overlord;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import lombok.Getter;
import lombok.Setter;
import zanoccio.javakit.LibraryUnpackingClassLoader;
import axirassa.overlord.exceptions.OverlordException;

public class Overlord {
	@Setter
	@Getter
	private String[] parameters;


	public static void main (String[] parameters) throws IOException, OverlordException, InterruptedException,
	        ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException,
	        IllegalAccessException, InvocationTargetException, InstantiationException {
		System.out.println("Loading OverlordMain");
		
		System.out.println("Testing for load from JAR file");
		ClassLoader loader = Overlord.class.getClassLoader();
		URL classUrl = loader.getResource(Overlord.class.getCanonicalName().replace('.', '/')+".class");
		String jarFile = OverlordUtilities.retrieveJarFile(classUrl);
		
		if(jarFile == null)
			System.out.println("Not running within a JAR file");
		else
			System.out.println("SOURCE JAR: "+jarFile);

		Class<?> classObject = Class.forName("axirassa.overlord.OverlordMain", true, new LibraryUnpackingClassLoader(jarFile));
		Method method = classObject.getMethod("main", String[].class);
		method.invoke(null, (Object) parameters);

		return;
	}
}
