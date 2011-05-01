
package axirassa.overlord

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.util.HashSet

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * {@link NativeLibraryProvider} extracts native libraries from JAR files into a
 * temporary directory where they may be directly accessed by the operating
 * system and loaded via {@link System.loadLibrary()}.
 *
 * Note that libraries are only extracted if they are on the classpath but
 * within a Jar file where they may not be accessed by the OS.
 *
 * @author wiktor
 *
 */
object NativeLibraryProvider {
  val BUFFER_SIZE = 65536
  val BYTE_BUFFER_SIZE = 4096
}

class NativeLibraryProvider {
  val logger = LoggerFactory.getLogger(classOf[NativeLibraryProvider])
  val availableLibraries = new HashSet[String]

  var tempdir : File = null

  private def getTemporaryDirectory() = {
    if (tempdir == null) {
      val tempfile = File.createTempFile("OverlordNativeLibrary", "")
      tempfile.delete()
      tempfile.mkdir()

      tempdir = tempfile
    }

    tempdir
  }

  /**
   * @return the path to the temporary directory containing provided libraries
   *         if necessary, or null if the libraries may be directly accessed
   *         by the OS.
   */
  def getLibraryPath() {
    if (tempdir == null)
      null
    else
      tempdir.getAbsolutePath()
  }

  def provideLibrary(name : String) {
    // check if we already provided the library
    if (availableLibraries.contains(name))
      return

    val library = getClass().getResource(name)
    if (library == null)
      throw new ExceptionInInitializerError("Cannot find resource on path: "+name)

    val source = new File(library.getPath())

    if (isWithinJar(source)) {
      val instream = getClass().getResourceAsStream(name)
      copyToDirectory(source, instream)
    }

    availableLibraries.add(name)
  }

  private def copyToDirectory(source : File, instream : InputStream) = {
    val directory = getTemporaryDirectory()
    val newfile = new File(directory.getPath() + File.separator + source.getName())

    logger.info("COPYING {} TO {}", source.getPath(), directory.getPath())

    val in = new BufferedInputStream(instream, NativeLibraryProvider.BUFFER_SIZE)
    val out = new BufferedOutputStream(new FileOutputStream(newfile), NativeLibraryProvider.BUFFER_SIZE)

    val bytebuffer = new Array[Byte](NativeLibraryProvider.BYTE_BUFFER_SIZE)

    var bytesread = 0
    do {
      bytesread = in.read(bytebuffer)
      out.write(bytebuffer, 0, bytesread)
    } while (bytesread > 0)

    in.close()
    out.close()
  }

  private def isWithinJar(file : File) = {
    val components = file.getPath().split("\\!", 2)
    val filename = components(0)

    filename.endsWith(".jar")
  }
}
