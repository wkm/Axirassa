
package axirassa.util;

public interface CSVRowWriter<T> {
	/**
	 * Generalized interface for efficiently writing an object as a CSV row
	 */
	public void writeRow(T row, StringBuilder builder);


	/**
	 * Method called at the start of each row
	 */
	public void startRow();


	/**
	 * Method called at the end of each row
	 */
	public void endRow();
}
