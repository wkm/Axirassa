
package axirassa.util;


import java.util.HashSet;

object Meta {
  private val primitives = new HashSet[Class[_]]

  primitives.add(classOf[Long])
  primitives.add(classOf[Int])
  primitives.add(classOf[String])
  primitives.add(classOf[Boolean])

  def inspect(obj : AnyRef) {
    val sb = new StringBuilder
    inspect(obj, 0, new HashSet[AnyRef], sb)

    println(sb.toString())
  }

  private def inspect(obj : AnyRef, indentlevel : Int, displayed : HashSet[AnyRef], buff : StringBuilder) {
    if (obj == null) {
      indent(buff, indentlevel);
      buff.append("null");
      return ;
    }

    val classObject = obj.getClass();
    if (primitives.contains(classObject)) {
      if (classObject == classOf[String]) {
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

      return ;
    }

    val fields = classObject.getDeclaredFields();

    buff.append(classObject.getCanonicalName());
    buff.append('@');
    buff.append(Integer.toHexString(obj.hashCode()));

    if (displayed.contains(obj))
      return ;

    if (classObject.isArray()) {
      val array = obj.asInstanceOf[Array[AnyRef]]

      buff.append('[');
      for (obj <- array) {
        buff.append('\n');
        inspect(obj, indentlevel + 1, displayed, buff);
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

      for (i <- 0 until fields.length) {
        buff.append('\n');
        indent(buff, indentlevel + 1);
        try {
          val previousaccessibility = fields(i).isAccessible();

          if (previousaccessibility == false)
            buff.append("      ");
          else
            buff.append("[pub] ");

          buff.append(fields(i).getName());
          buff.append(" = ");

          fields(i).setAccessible(true);

          inspect(fields(i).get(obj), indentlevel + 1, displayed, buff);

          fields(i).setAccessible(previousaccessibility);
        } catch {
          case e : Exception =>
            buff.append("<exception: "+e.getClass().getCanonicalName()+">");
        }
      }

      if (fields.length > 0) {
        buff.append('\n');
        indent(buff, indentlevel);
      }

      buff.append("}");

    }
  }

  private def indent(sb : StringBuilder, level : Int) {
    for (i <- 0 until level) {
      sb.append("  ")
    }
  }
}
