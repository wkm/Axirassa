
package axirassa.overlord

import java.util.Iterator

import org.w3c.dom.Node
import org.w3c.dom.NodeList

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
class IterableNodeList(nodelist : NodeList) extends Iterable[Node] {
  override def iterator() : Iterator[Node] = new NodeListIterator(nodelist)
}

class NodeListIterator(nodelist : NodeList) extends Iterator[Node] {
  private var index = 0

  override def hasNext() = {
    if (index < nodelist.getLength())
      true
    else
      false
  }

  override def next() = {
    index += 1
    nodelist.item(index - 1)
  }

  override def remove() {
    throw new UnsupportedOperationException()
  }

}
