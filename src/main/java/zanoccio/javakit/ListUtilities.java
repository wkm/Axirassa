
package zanoccio.javakit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListUtilities {

	public interface ListPartitioner<T> {
		/**
		 * @return true if the list should be partitioned at the current
		 *         position, based on the previous and current element.
		 */
		public boolean partition(T previous, T current);
	}


	public static <T> List<List<T>> partition(List<T> list, ListPartitioner<T> partitioner) {
		ArrayList<List<T>> lists = new ArrayList<List<T>>();

		// handle degenerate cases
		if (list.size() <= 1) {
			lists.add(list);
			return lists;
		}

		int previoussplit = 0;
		int index = 0;

		Iterator<T> iter = list.iterator();
		T previous = iter.next();
		T current = null;

		// loop over the list, splitting where applicable
		while (iter.hasNext()) {
			index++;
			current = iter.next();

			if (partitioner.partition(previous, current)) {
				lists.add(list.subList(previoussplit, index));
				previoussplit = index;
			}

			previous = current;
		}

		// shove any remaining items onto the list
		lists.add(list.subList(previoussplit, index));

		return lists;
	}
}
