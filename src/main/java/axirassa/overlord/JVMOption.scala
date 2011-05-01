package axirassa.overlord

object JVMOption extends Enumeration {
  type JVMOption = Value

  val XMX = Value("Xmx")
  val XMS = Value("Xms")
  val XSS = Value("Xss")
}