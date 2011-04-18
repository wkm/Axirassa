
package axirassa.util;

import java.lang.reflect.Field;
import java.util.HashSet;

public class Meta {
	public static HashSet<Class<? extends Object>> primitives = new HashSet<Class<? extends Object>>();
	static {
		primitives.add(Long.class);
		primitives.add(Integer.class);
		primitives.add(String.class);
		primitives.add(Boolean.class);
	}


	public static void inspect (Object obj) {
		StringBuilder buff = new StringBuilder();
		inspect(obj, 0, new HashSet<Object>(), buff);

		System.out.println(buff.toString());
	}


	private static void inspect (Object obj, int indentlevel, HashSet<Object> displayed, StringBuilder buff) {
		if (obj == null) {
			indent(buff, indentlevel);
			buff.append("null");
			return;
		}

		Class<? extends Object> classObject = obj.getClass();
		if (primitives.contains(classObject)) {
			if (classObject == String.class) {
				buff.append("\"");
				buff.append(obj);
				buff.append("\"");
			} else
				buff.append(obj);

			buff.append(' ');
			buff.append('(');
			buff.append(classObject.getCanonicalName()).append('@');
			buff.append(Integer.toHexString(obj.hashCode()));
			buff.append(')');

			return;
		}

		Field[] fields = classObject.getDeclaredFields();

		buff.append(classObject.getCanonicalName());
		buff.append('@');
		buff.append(Integer.toHexString(obj.hashCode()));

		if (displayed.contains(obj))
			return;

		if (classObject.isArray()) {
			Object[] array = (Object[]) obj;

			buff.append('[');
			for (Object object : array) {
				buff.append('\n');
				inspect(object, indentlevel + 1, displayed, buff);
			}

			if (array.length > 0) {
				buff.append('\n');
				indent(buff, indentlevel);
			}
			buff.append(']');
		}

		if (fields.length > 0) {
			displayed.add(obj);

			buff.append("{");

			for (int i = 0; i < fields.length; i++) {
				buff.append('\n');
				indent(buff, indentlevel + 1);
				try {
					boolean previousaccessibility = fields[i].isAccessible();

					if (previousaccessibility == false)
						buff.append("      ");
					else
						buff.append("[pub] ");

					buff.append(fields[i].getName());
					buff.append(" = ");

					fields[i].setAccessible(true);

					inspect(fields[i].get(obj), indentlevel + 1, displayed, buff);

					fields[i].setAccessible(previousaccessibility);
				} catch (Exception e) {
					buff.append("<exception: " + e.getClass().getCanonicalName() + ">");
				}
			}

			if (fields.length > 0) {
				buff.append('\n');
				indent(buff, indentlevel);
			}

			buff.append("}");

		}
	}


	private static void indent (StringBuilder buff, int level) {
		for (int i = 0; i < level; i++)
			buff.append("  ");
	}
}
