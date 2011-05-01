
package axirassa.overlord

import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.List
import java.util.Map.Entry

import scala.collection.JavaConversions._

class ExecutionTargetOptions {

  val options = new LinkedHashMap[JVMOption.JVMOption, String]
  var libraries = new ArrayList[String]

  def addJVMOption(option : String, value : String) {
    addJVMOption(JVMOption.withName(option.toUpperCase()), value)
  }

  def addJVMOption(option : JVMOption.JVMOption, value : String) {
    options.put(option, value)
  }

  def addLibrary(name : String) {
    libraries.add(name)
  }

  def getCommandLine = {
    val cli = new ArrayList[String]
    for (entry <- options.entrySet()) {
      val sb = new StringBuilder(30)
      sb.append('-').append(entry.getKey().toString()).append(entry.getValue())
      cli.add(sb.toString())
    }

    cli
  }

  /**
   * @return true if this target requires native libraries
   */
  def needsLibraries = libraries.size() > 0

  /**
   * @return the number of JVM options
   */
  def size = options.size
}
