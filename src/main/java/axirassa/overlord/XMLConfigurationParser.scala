
package axirassa.overlord

import java.io.File
import java.io.InputStream
import java.net.URL

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList

import zanoccio.javakit.ClassPathEntityResolver

class XMLConfigurationParser(
  configfile : URL,
  stream : InputStream,
  configuration : OverlordConfiguration) {

  var dom : Document = null
  var docroot : Element = null

  def parse() = {
    val dbf = DocumentBuilderFactory.newInstance()

    setClassPathAndBaseDirectory()

    try {
      val db = dbf.newDocumentBuilder()
      db.setEntityResolver(new ClassPathEntityResolver())
      dom = db.parse(stream)
    } catch {
      case e : Exception =>
        throw new OverlordParsingException(dom, e)
    }

    docroot = dom.getDocumentElement()

    createExecutionTargets()
    createExecutionGroups()

    configuration
  }

  private def setClassPathAndBaseDirectory() {
    // if the configfile is within a JAR, we use it to set the classpath
    val jarfile = retrieveJarFile(configfile)
    if (jarfile != null) {
      configuration.classPath = stripPrefix(jarfile)
      configuration.baseDirectory = stripPrefix(new File(jarfile).getParent())
    } else {
      configuration.baseDirectory = new File(configfile.getPath()).getParent()
    }
  }

  private def stripPrefix(path : String) = path.replaceFirst("^file:", "").replaceFirst("^file:", "")

  private def retrieveJarFile(file : URL) = {
    val components = file.getPath().split("!", 2)
    if (components.length < 2)
      null
    else if (components(0).toLowerCase().endsWith(".jar"))
      components(0)
    else null
  }

  private def createExecutionTargets() {
    val targetlist = docroot.getElementsByTagName(XMLName.TARGET.toString())
    if (targetlist.getLength() < 1)
      throw new NoExecutionTargetsException(dom)

    for (targetnode <- new IterableNodeList(targetlist)) {
      val target = createExecutionTarget(targetnode)

      // check that this execution target doesn't already exist
      if (configuration.hasExecutionTarget(target.getCanonicalName()))
        throw new DuplicateTargetException(target, dom)

      configuration.addExecutionTarget(target)
    }
  }

  private def createExecutionTarget(node : Node) {
    val attributes = node.getAttributes()

    val nameNode = attributes.getNamedItem(XMLName.NAME.toString())
    val classNode = attributes.getNamedItem(XMLName.CLASS.toString())
    val autorestartNode = attributes.getNamedItem(XMLName.AUTORESTART.toString())

    val name = nameNode.getTextContent()
    val classname = classNode.getTextContent()
    val autorestart = Boolean.parseBoolean(autorestartNode.getTextContent())

    try {
      var target = new ExecutionTarget(name, classname)
      // apply any options
      val options = createExecutionTargetOptions(node.getChildNodes())
      target.options = options

      target.autoRestart = autorestart
      target
    } catch {
      case e : ClassNotFoundException =>
        throw new OverlordTargetClassNotFoundException(name, node.getOwnerDocument(), e)
    }
  }

  private def createExecutionTargetOptions(nodelist : NodeList) = {
    val options = new ExecutionTargetOptions()

    for (node <- new IterableNodeList(nodelist)) {
      val nodename = node.getNodeName().toLowerCase()

      if (nodename.equals(XMLName.JVMOPTION.toString()))
        createExecutionTargetJVMOption(options, node)
      else if (nodename.equals(XMLName.LIBRARY.toString()))
        createExecutionTargetLibraryOption(options, node)
    }

    options
  }

  private def createExecutionTargetJVMOption(options : ExecutionTargetOptions, node : Node) {
    val attributes = node.getAttributes()
    val namenode = attributes.getNamedItem(XMLName.NAME.toString())
    val valuenode = attributes.getNamedItem(XMLName.VALUE.toString())

    options.addJVMOption(namenode.getTextContent(), valuenode.getTextContent())
  }

  private def createExecutionTargetLibraryOption(options : ExecutionTargetOptions, node : Node) {
    options.addLibrary(node.getTextContent())
  }

  private def createExecutionGroups() {
    val grouplist = docroot.getElementsByTagName(XMLName.GROUP.toString())
    if (grouplist.getLength() < 1)
      throw new NoGroupsException(dom)

    for (groupnode <- new IterableNodeList(grouplist)) {
      val group = createExecutionGroup(groupnode)

      if (configuration.hasExecutionGroup(group.getCanonicalName()))
        throw new DuplicateGroupException(group, dom)

      configuration.addExecutionGroup(group)
    }
  }

  private def createExecutionGroup(node : Node) = {
    // create an empty execution group
    val attributes = node.getAttributes()

    val namenode = attributes.getNamedItem(XMLName.NAME.toString())
    val name = namenode.getTextContent()

    val group = new ExecutionGroup(name)

    // fill it with execution targets
    val childnodes = node.getChildNodes()

    if (childnodes.getLength() < 1)
      throw new EmptyExecutionGroupException(name, node.getOwnerDocument())

    for (childnode <- new IterableNodeList(childnodes))
      if (childnode.getNodeName().equals(XMLName.EXECUTE.toString()))
        group.addExecutionSpecification(createExecutionSpecification(childnode))

    group
  }

  private def createExecutionSpecification(node : Node) = {
    val attributes = node.getAttributes()

    val targetNameAttr = attributes.getNamedItem(XMLName.TARGET.toString()).getTextContent()
    val instancecountAttr = attributes.getNamedItem(XMLName.INSTANCES.toString()).getTextContent()
    val initialDelayAttr = attributes.getNamedItem(XMLName.INITIALDELAY.toString()).getTextContent()
    val instancecount = Integer.parseInt(instancecountAttr)
    val initialDelay = Integer.parseInt(initialDelayAttr)

    val target = configuration.getExecutionTarget(targetNameAttr)

    if (target == null)
      throw new UnknownExecutionTargetException(targetNameAttr, node.getOwnerDocument())

    val spec = new ExecutionSpecification(configuration, target)
    spec.instances = instancecount
    spec.initialDelay = initialDelay

    spec
  }
}
