package axirassa.overlord

object JVMOption extends Enumeration {
  type JVMOption = Value

  val XMX = Value("XMX")
  val XMS = Value("XMS")
  val XSS = Value("XSS")
}