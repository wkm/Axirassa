package axirassa.model

import scalakit.EnumvType

object MonitorType extends EnumvType(MonitorType : Enumeration) {
  type MonitorType = Enumeration

	val ICMP_PING = Value("ICMP_PING")
	
	val HTTP = Value("HTTP")
	val HTTPS = Value("HTTPS")
	
	val IMAP = Value("IMAP")
	val IMAPS = Value("IMAPS")
	
	val POP = Value("POP")
	val POPS = Value("POPS")
	
	val SMTP = Value("SMTP")
	val SMTPS = Value("SMTPS")
}