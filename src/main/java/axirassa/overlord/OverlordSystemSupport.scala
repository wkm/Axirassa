package axirassa.overlord

trait OverlordSystemSupport {
  def javaExecutable: String
}

object OverlordSystemSupport {
  def getSystemSupport: OverlordSystemSupport =
    getSystemSupport(System.getProperty("os.name").toLowerCase)

  def getSystemSupport(osName: String) = {
    val windowsRegex = "(?i).*windows.*".r
    val linuxRegex = "(?i).*linux.*".r
    val macRegex = "(?i).*mac.*".r

    osName match {
      case windowsRegex() => new WindowsSystemSupport
      case linuxRegex() => new LinuxSystemSupport
      case macRegex() => new MacSystemSupport
      case _ => throw new UnsupportedPlatformException(osName)
    }
  }
}

case class WindowsSystemSupport() extends OverlordSystemSupport {
  override def javaExecutable = "java.exe"
}

case class MacSystemSupport() extends OverlordSystemSupport {
  override def javaExecutable = "java"
}

case class LinuxSystemSupport() extends OverlordSystemSupport {
  override def javaExecutable = "java"
}