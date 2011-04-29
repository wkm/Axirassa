
package axirassa.overlord

object OverlordUtilities {
	val VALID_NAME_PATTERN = "[a-zA-Z_][a-zA-Z0-9_]*"

	/**
	 * @return true if the given name matches {@value #VALID_NAME_PATTERN}.
	 */
	def isValidName(name : String) = name.matches(VALID_NAME_PATTERN)
}
