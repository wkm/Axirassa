package scalakit

object RegexpPattern {
    def apply(pattern : String) = new RegexpPattern(pattern)
    def unapply(text : String) = Some(text)
}

class RegexpPattern(pattern: String) {
	val regexp = ("(" + pattern + ")").r
	def equals(text : String) = regexp.pattern.matcher(text).matches
}