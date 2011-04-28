package axirassa.model

import scalakit.EnumvType
import scalakit.Enumv

object MonitorType extends Enumv {
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

class MonitorType extends EnumvType(MonitorType)