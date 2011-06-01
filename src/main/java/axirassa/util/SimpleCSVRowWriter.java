
package axirassa.util;

abstract public class SimpleCSVRowWriter<T> implements CSVRowWriter<T> {
	private int columnId = 0;


	public void writeCell(StringBuilder sb, Object object) {
		if (columnId > 0)
			sb.append(';');

		sb.append(object);
		columnId++;
	}


	public void startRow() {
		columnId = 0;
	}


	public void endRow() {
		// empty
	}
}
