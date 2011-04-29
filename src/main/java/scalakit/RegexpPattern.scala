package scalakit

object RegexpPattern {
	def apply(pattern : String) = new RegexpPattern(pattern)
	def unapply(text : String) = Some(text)

	def main(args : Array[String]) {
		"fiddle" match {
			case RegexpPattern("f.*") => println("matched pattern 1")
			case _ => println("didn't match any pattern")
		}
	}
}

class RegexpPattern(pattern : String) {
	val regexp = ("("+pattern+")").r
	def equals(text : String) = regexp.pattern.matcher(text).matches
}