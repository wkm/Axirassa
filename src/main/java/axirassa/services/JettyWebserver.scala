
package axirassa.services

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.util.Enumeration
import java.util.jar.JarEntry
import java.util.jar.JarFile

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import org.eclipse.jetty.xml.XmlConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object JettyWebserver {
  val log = LoggerFactory.getLogger(classOf[JettyWebserver])
  val BASE_PATH = "webapp/"
}

class JettyWebserver extends Service {
  def main(args : Array[String]) {
    val service = new JettyWebserver()
    service.execute
  }

  val log = JettyWebserver.log

  override def execute {
    val server = new Server()

    val classLoader = getClass().getClassLoader()

    val jettyConfig = classLoader.getResourceAsStream("jetty.xml")
    val configuration = new XmlConfiguration(jettyConfig)
    configuration.configure(server)

    val baseLocation = getClass().getProtectionDomain().getCodeSource().getLocation()

    if (baseLocation == null) {
      log.warn("COULD NOT LOCATE RESOURCE: "+JettyWebserver.BASE_PATH)
      log.warn("Assuming developmental deployment")

      val handler = new WebAppContext()

      handler.setDescriptor("src/main/webapp/WEB-INF/web.xml")
      handler.setResourceBase("src/main/webapp")
      handler.setContextPath("/")
      handler.setParentLoaderPriority(true)
      handler.setServer(server)

      server.setHandler(handler)
    } else {
      log.info("Extracting jar file")
      val basePath = extractJarContents("/Users/wiktor/PCode/X/target/axir-distribution.jar")

      val handler = new WebAppContext()
      handler.setContextPath("/")
      handler.setDescriptor(basePath+"/webapp/WEB-INF/web.xml")
      handler.setResourceBase(basePath+"/webapp")
      handler.setServer(server)
      server.setHandler(handler)
    }

    server.start()

  }

  private def extractJarContents(jarFile : String) {
    val dir = getTemporaryDirectory()
    val jar = new JarFile(jarFile)

    val entries = jar.entries()
    while (entries.hasMoreElements()) {
      val entry = entries.nextElement()
      val outFile = new File(dir, entry.getName())

      if (entry.isDirectory()) {
        log.trace("Directory: {}", entry)
        outFile.mkdirs()
      } else {

        log.trace("Extracting {} to {}\n", entry.getName(), outFile.getAbsolutePath())
        try {

          val in = new BufferedInputStream(jar.getInputStream(entry))
          val out = new BufferedOutputStream(new FileOutputStream(outFile))

          val buffer = new Array[Byte](4096)
          var bytesRead = 0
          do {
            bytesRead = in.read(buffer)
            if (bytesRead > 0)
              out.write(buffer, 0, bytesRead)
          } while (bytesRead > 0)

          out.flush()
          out.close()

          in.close()
        } catch {
          case e : IOException =>
            log.warn("Exceptions when extracting: {} {}", entry.getName(), e.getMessage())
        }
      }
    }

    dir.getPath()
  }

  private def getTemporaryDirectory() = {
    val tempfile = File.createTempFile("axoverlord", "")
    tempfile.delete()
    tempfile.mkdir()

    tempfile
  }
}
