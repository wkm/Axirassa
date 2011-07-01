
package test.axirassa.mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * 
 * @author wiktor
 */
public class TestMocking {

	@Test
	public void testInteractions() {
		@SuppressWarnings("unchecked")
		List<String> mockedList = mock(List.class);

		mockedList.add("one");
		mockedList.clear();

		verify(mockedList).add("one");
		verify(mockedList).clear();
	}


	@Test
	public void testMethodStubbing() {
		@SuppressWarnings("unchecked")
		LinkedList<String> mocked = mock(LinkedList.class);

		when(mocked.get(0)).thenReturn("first");

		assertEquals("first", mocked.get(0));
		assertNull(mocked.get(99));
	}


	@Test(expected = ExceptionInInitializerError.class)
	public void argumentMatching() {
		LinkedList mocked = mock(LinkedList.class);
		when(mocked.add(any())).thenThrow(new ExceptionInInitializerError("hi"));

		mocked.add("foo");
	}
}
