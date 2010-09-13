
package com.zanoccio.javakit;

/**
 * A collection of various {@link String} formatting utility functions
 * 
 * @author wiktor
 */
public class FormatString {

	/**
	 * Like {@link #cutoff(String, int, String)} but with a default ellipsis of
	 * <tt>".."</tt>
	 */
	public static String cutoff(String text, int width) {
		return cutoff(text, width, "..");
	}


	/**
	 * Cuts off a string to be less than the given width. The given ellipsis is
	 * used to indicate that string was cutoff.
	 */
	public static String cutoff(String text, int width, String ellipsis) {
		// if the string already fits, do nothing
		if (text.length() <= width)
			return text;

		int take = width - ellipsis.length();

		StringBuilder sb = new StringBuilder(width);
		sb.append(text.substring(0, take));
		sb.append(ellipsis);

		return sb.toString();
	}


	/**
	 * Like {@link #center(String, int, char)} but with a default background of
	 * spaces.
	 */
	public static String center(String text, int width) {
		return center(text, width, ' ');
	}


	/**
	 * Centers a string within the given width on the given background of
	 * characters.
	 * 
	 * Strings which are longer than the specified width are not trimmed.
	 */
	public static String center(String text, int width, char background) {
		// nothing to center
		if (text.length() >= width)
			return text;

		StringBuilder sb = new StringBuilder(width);

		// compute padding
		int left = (width - text.length()) / 2;
		int right = width - text.length() - left;

		for (int i = 0; i < left; i++)
			sb.append(background);

		sb.append(text);

		for (int i = 0; i < right; i++)
			sb.append(background);

		return sb.toString();
	}
}
