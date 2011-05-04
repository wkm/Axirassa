package axirassa.model

object TriggerSetting extends Enumeration {
  val TIME = Value("TI")
  val SIZE = Value("SZ")
  val IGNORE_WARNING_ERROR = Value("IWE")
  val REQUIRE_IGNORE_DONTALLOW = Value("RID")
}