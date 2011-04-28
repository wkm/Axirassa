package axirassa.overlord

import org.junit.runner.RunWith
import org.specs._
import org.specs.matcher._
import org.specs.runner.{ JUnitSuiteRunner, JUnit }

@RunWith(classOf[JUnitSuiteRunner])
class OverlordSystemSupportSpecTest extends Specification with JUnit {
    "OverlordSystemSupport" should {
        val oss = OverlordSystemSupport 

        "recognize windows" in {
            oss.getSystemSupport("MS WINDOWS") must haveClass[WindowsSystemSupport]
        }

        "recognize os x" in {
            oss.getSystemSupport("Apple Macintosh") must haveClass[MacSystemSupport]
        }

        "recognize linux" in {
            oss.getSystemSupport("Linux OS") must haveClass[LinuxSystemSupport]
        }
    }
}

object OverlordSystemSupportSpecMain {
    def main(args: Array[String]) {
        new OverlordSystemSupportSpecTest().main(args)
    }
}
