
package axirassa.overlord

object XMLName {
	// elements
	val TARGET = new XMLName("TARGET")
	val GROUP = new XMLName("GROUP")
	val EXECUTE = new XMLName("EXECUTE")
	val JVMOPTION = new XMLName("JVMOPTION")
	val LIBRARY = new XMLName("LIBRARY")

	// attributes
	val INSTANCES = new XMLName("INSTANCES")
	val NAME = new XMLName("NAME")
	val CLASS = new XMLName("CLASS")
	val VALUE = new XMLName("VALUE")
	val INITIALDELAY = new XMLName("INITIALDELAY")
	val AUTORESTART = new XMLName("AUTORESTART")
}

class XMLName(var string : String) {
	def toString = string
}
