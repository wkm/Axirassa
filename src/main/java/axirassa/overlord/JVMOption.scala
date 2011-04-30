package axirassa.overlord

object JVMOption {
  val XMX = new JVMOption("Xmx")
  val XMS = new JVMOption("Xms")
  val XSS = new JVMOption("Xss")
}
class JVMOption(val name : String) {
  override def toString = name
}


