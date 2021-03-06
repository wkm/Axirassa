
package packetkit;

/**
 * An implementation of a byte-level trie.
 * 
 * Extremely naive benchmarks seem to indicate that this runs at around 30MHz.
 * 
 * @author wiktor
 * 
 * @param <T>
 */
public class ByteTrie<T> {

	private ByteTrieIndex<T> head;
	private final int nodecount = 0;


	public ByteTrie() {
		head = null;
	}


	public void add(byte[] key, T value) {
		if (head == null && key.length > 0) {
			head = new ByteTrieIndex<T>();
			head.bytevalue = key[0];
		}

		ByteTrieIndex<T> cursor = head;
		int index = 0;
		while (true) {
			if (cursor.bytevalue == key[index]) {
				// are we done?
				if (index == key.length - 1) {
					// override the existing value
					cursor.value = value;
					return;
				}

				// go to the child
				if (cursor.child == null) {
					// fast-create the chain of children
					cursor.child = createChain(key, value, index + 1);
					return;
				}

				// the child exists, so go
				cursor = cursor.child;
				index++;

			} else {
				// go to the next node
				if (cursor.next == null) {
					cursor.next = createChain(key, value, index);
					return;
				}

				cursor = cursor.next;
			}
		}
	}


	private ByteTrieIndex<T> createChain(byte[] key, T value, int index) {
		ByteTrieIndex<T> head = new ByteTrieIndex<T>();
		ByteTrieIndex<T> cursor = head;

		for (; index < key.length - 1; index++) {
			cursor.child = new ByteTrieIndex<T>();
			cursor.bytevalue = key[index];
			cursor = cursor.child;
		}

		cursor.bytevalue = key[index];
		cursor.value = value;

		return head;
	}


	public T get(byte[] key) {
		return get(key, 0, key.length);
	}


	public T get(byte[] key, int start, int length) {
		ByteTrieIndex<T> cursor = head;

		int index = start;
		int end = start + length - 1;
		while (true) {
			if (index >= key.length)
				return null;

			if (cursor == null)
				return null;

			if (cursor.bytevalue == key[index]) {
				// are we done?
				if (index == end)
					return cursor.value;

				// now look at the child
				cursor = cursor.child;
				index++;
			} else
				cursor = cursor.next;
		}
	}
}

class ByteTrieIndex<T> {
	byte bytevalue;
	T value;
	ByteTrieIndex<T> child;
	ByteTrieIndex<T> next;


	public ByteTrieIndex() {
		this.bytevalue = 0;
		this.value = null;
		this.child = null;
		this.next = null;
	}


	public ByteTrieIndex(int bytevalue, T value) {
		this.bytevalue = (byte) bytevalue;
		this.value = value;
		this.child = null;
		this.next = null;
	}


	@Override
	public String toString() {
		return super.toString() + "  " + bytevalue + ": " + value;
	}
}
