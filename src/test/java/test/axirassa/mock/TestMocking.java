package test.axirassa.mock;

import org.junit.Test;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 *
 * @author wiktor
 */
public class TestMocking {

    @Test
    public void testInteractions () {
        List mockedList = mock(List.class);

        mockedList.add("one");
        mockedList.clear();

        verify(mockedList).add("one");
        verify(mockedList).clear();
    }


    @Test
    public void testMethodStubbing () {
        LinkedList mocked = mock(LinkedList.class);

        when(mocked.get(0)).thenReturn("first");

        assertEquals("first", mocked.get(0));
        assertNull(mocked.get(99));
    }


}
