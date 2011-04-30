
package zanoccio.javakit

/**
 * A collection of various {@link String} formatting utility functions
 * 
 * @author wiktor
 */
object FormatString {

	/**
	 * Cuts off a string to be less than the given width. The given ellipsis is
	 * used to indicate that string was cutoff.
	 */
	def cutoff(text :String , width :Int , ellipsis :String  = "..") : String = {
		// if the string already fits, do nothing
		if (text.length() <= width)
			return text

		val take = width - ellipsis.length()

		val sb = new StringBuilder(width)
		sb.append(text.substring(0, take))
		sb.append(ellipsis)

		sb.toString()
	}


	/**
	 * Centers a string within the given width on the given background of
	 * characters.
	 * s
	 * Strings which are longer than the specified width are not trimmed.
	 */
	def center(text : String , width : Int, background : Char  = ' ') : String = {
		// nothing to center
		if (text.length() >= width)
			return text

		val sb = new StringBuilder(width)

		// compute padding
		val left = (width - text.length()) / 2
		val right = width - text.length() - left

		for (i <- 0 until left)
			sb.append(background)

		sb.append(text)

		for (i <- 0 until right)
			sb.append(background)

		return sb.toString()
	}
}
