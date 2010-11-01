
package com.zanoccio.axirassa.overlord;

import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Convenience class for wrapping a {@link NodeList} when iterating over it
 * within a for-loop.
 * 
 * For example:
 * 
 * <pre>
 * for (Node node : new IterableNodeList(nodelist)) {
 * 	// do something with node
 * }
 * </pre>
 * 
 * @author wiktor
 */
public class IterableNodeList implements Iterable<Node> {
	private final NodeList nodelist;


	public IterableNodeList(NodeList nodelist) {
		this.nodelist = nodelist;
	}


	@Override
	public Iterator<Node> iterator() {
		return new NodeListIterator(nodelist);
	}
}

class NodeListIterator implements Iterator<Node> {

	private int index = 0;
	private final NodeList nodelist;


	public NodeListIterator(NodeList nodelist) {
		this.nodelist = nodelist;
	}


	@Override
	public boolean hasNext() {
		if (index < nodelist.getLength())
			return true;
		else
			return false;
	}


	@Override
	public Node next() {
		index++;
		return nodelist.item(index - 1);
	}


	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
