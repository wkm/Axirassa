
package axirassa.util;

import java.util.List;

import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.dom.Node;

public class ElementUtil {
	public static Node getNodeBefore(Node element) {
		Element container = element.getContainer();
		if (container == null)
			return null;

		List<Node> children = container.getChildren();
		Node previous = null;
		for (Node cursor : children) {
			if (cursor == element)
				return previous;

			previous = cursor;
		}

		return null;
	}


	public static Element getElementBefore(Node element) {
		Node nodeBefore = getNodeBefore(element);

		if (nodeBefore instanceof Element)
			return (Element) nodeBefore;
		else
			return null;
	}


	public static Node getNodeAfter(Node element) {
		Element container = element.getContainer();
		if (container == null)
			return null;

		List<Node> children = container.getChildren();
		Node previous = null;
		for (Node cursor : children) {
			if (previous == element)
				return cursor;

			previous = cursor;
		}

		return null;
	}


	public static Element getElementAfter(Node element) {
		Node nodeAfter = getNodeAfter(element);

		if (nodeAfter instanceof Element)
			return (Element) nodeAfter;
		else
			return null;
	}
}
