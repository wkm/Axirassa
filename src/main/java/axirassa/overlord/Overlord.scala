
package axirassa.overlord

import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.ArrayList
import java.util.Collection
import java.util.List

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._

/**
 * A process starting/monitoring daemon and framework.
 *
 * AxOverlord hooks into a HornetQ message passing framework.
 *
 * Note that java processes started by Overlord will not be terminated by
 * default unless {@link #addShutdownHooks()} is called.
 *
 * @author wiktor
 *
 */
object Overlord {
  val CONFIGURATION_FILE = "axoverlord.cfg.xml"

  def main(arg : Array[String]) {
    val overlord = new Overlord()
    overlord.addShutdownHooks()

    if (arg.length <= 0)
      overlord.execute(Array("master"))
    else
      overlord.execute(arg)
  }

  var execId = 0

  def getNextExecID() = {
    synchronized {
      execId += 1
      execId
    }
  }
}

class Overlord {
  val logger = LoggerFactory.getLogger(classOf[Overlord])

  //
  // Class Instances
  //

  var configuration : OverlordConfiguration = _
  var instances = new ArrayList[ExecutionInstance]
  var libraryProvider = new NativeLibraryProvider

  def execute(parameters : Array[String]) {
    val systemsupport = OverlordSystemSupport.getSystemSupport

    val configfile = ClassLoader.getSystemResource(Overlord.CONFIGURATION_FILE)
    if (configfile == null)
      throw new NoOverlordConfigurationException(Overlord.CONFIGURATION_FILE)
    val configstream = ClassLoader.getSystemResourceAsStream(Overlord.CONFIGURATION_FILE)

    configuration = new OverlordConfiguration(this)
    configuration.javaExecutable = systemsupport.javaExecutable

    val configparser = new XMLConfigurationParser(configfile, configstream, configuration)
    configparser.parse()

    val groups = new ArrayList[ExecutionGroup]()

    for (groupname <- parameters) {
      var group : ExecutionGroup = null
      if (groupname.matches("t:.*")) {
        val targetName = groupname.substring(2)

        val target = configuration.getExecutionTarget(targetName)
        if (target == null)
          throw new UnknownExecutionTargetException(targetName, null)

        val spec = new ExecutionSpecification(configuration, target)
        spec.instances = 1

        group = new ExecutionGroup("target_"+targetName)
        group.addExecutionSpecification(spec)
      } else {
        group = configuration.getExecutionGroup(groupname)
      }

      if (group != null)
        groups.add(group)
      else {
        logger.error("Unknown Execution Group: {}", group)
        return
      }
    }

    for (group <- groups)
      group.execute()
  }

  def addShutdownHooks() {
    Runtime.getRuntime().addShutdownHook(new OverlordDynamicShutdownHook(this))
  }

  def addExecutionInstance(thread : Thread, monitor : ExecutionMonitor) {
    instances.add(new ExecutionInstance(thread, monitor))
  }

  def killInstances() {
    for (instance <- instances)
      if (instance.thread.isAlive()) {
        instance.thread.interrupt()
        instance.monitor.killProcess()
      }
  }
}

class ExecutionInstance(val thread : Thread, val monitor : ExecutionMonitor)