
package axirassa.overlord

object XMLName {
  // elements
  val TARGET = new XMLName("target")
  val GROUP = new XMLName("group")
  val EXECUTE = new XMLName("execute")
  val JVMOPTION = new XMLName("jvmoption")
  val LIBRARY = new XMLName("library")

  // attributes
  val INSTANCES = new XMLName("instances")
  val NAME = new XMLName("name")
  val CLASS = new XMLName("class")
  val VALUE = new XMLName("value")
  val INITIALDELAY = new XMLName("initialdelay")
  val AUTORESTART = new XMLName("autorestart")
}

class XMLName(string : String) {
  override def toString = string
}
