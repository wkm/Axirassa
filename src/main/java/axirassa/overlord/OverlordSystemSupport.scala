package axirassa.overlord;

import axirasa.overlord.UnsupportedPlatformException
import scalakit.RegexpPattern

trait OverlordSystemSupport {
    def getJavaExecutable : String
}

object OverlordSystemSupport {
    def getSystemSupport : OverlordSystemSupport =
    	getSystemSupport(System.getProperty("os.name").toLowerCase)
    
    def getSystemSupport(osName : String) =
        osName match {
            case RegexpPattern(".*windows.*") => new WindowsSystemSupport
            case RegexpPattern(".*linux.*") => new LinuxSystemSupport
            case RegexpPattern(".*mac.*") => new MacSystemSupport
            case _ => throw new UnsupportedPlatformException(osName)
        }
}

case class WindowsSystemSupport() extends OverlordSystemSupport {
    override
    def getJavaExecutable = "java.exe"
}

case class MacSystemSupport() extends OverlordSystemSupport {
    override
    def getJavaExecutable = "java"
}

case class LinuxSystemSupport() extends OverlordSystemSupport {
    override
    def getJavaExecutable = "java"
}