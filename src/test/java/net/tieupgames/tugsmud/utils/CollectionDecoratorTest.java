package net.tieupgames.tugsmud.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CollectionDecoratorTest {

    private final Collection<Object> mockCollection = mock(Collection.class);
    private final CollectionDecorator<Object> instance = new CollectionDecorator<>(mockCollection);

    private final Object someObject = new Object();
    private final Collection<Object> anotherCollection = new ArrayList<>();

    @Test
    public void constructorRejectsNulls() {
        try {
            new CollectionDecorator<>(null);
            fail("constructor should have thrown a NullPointerException");
        } catch (NullPointerException e) {}
    }

    @Test
    public void size() throws Exception {
        int expected = (int)(Math.random() * 1000);
        when(mockCollection.size()).thenReturn(expected);
        int result = instance.size();
        verify(mockCollection).size();
        assertEquals(expected, result);
    }

    @Test
    public void isEmpty() throws Exception {
        boolean expected = true;
        when(mockCollection.isEmpty()).thenReturn(expected);
        boolean result = instance.isEmpty();
        verify(mockCollection).isEmpty();
        assertEquals(expected, result);
    }

    @Test
    public void contains() throws Exception {
        boolean expected = true;
        when(mockCollection.contains(someObject)).thenReturn(expected);
        boolean result = instance.contains(someObject);
        verify(mockCollection).contains(someObject);
        assertEquals(expected, result);
    }

    @Test
    public void iterator() throws Exception {
        @SuppressWarnings("unchecked")
        Iterator<Object> expected = mock(Iterator.class);
        when(mockCollection.iterator()).thenReturn(expected);
        Iterator<Object> result = instance.iterator();
        verify(mockCollection).iterator();
        assertEquals(expected, result);
    }

    @Test
    public void toArray() throws Exception {
        Object[] expected = new Object[] { new Object(), new Object() };
        when(mockCollection.toArray()).thenReturn(expected);
        Object[] result = instance.toArray();
        verify(mockCollection).toArray();
        assertArrayEquals(expected, result);
    }

    @Test
    public void toArray1() throws Exception {
        Object[] someArray = new Object[0];
        Object[] expected = new Object[] { new Object(), new Object() };
        when(mockCollection.toArray(any())).thenReturn(expected);
        Object[] result = instance.toArray(someArray);
        verify(mockCollection).toArray(someArray);
        assertArrayEquals(expected, result);
    }

    @Test
    public void add() throws Exception {
        boolean expected = true;
        when(mockCollection.add(someObject)).thenReturn(expected);
        boolean result = instance.add(someObject);
        verify(mockCollection).add(someObject);
        assertEquals(expected, result);
    }

    @Test
    public void remove() throws Exception {
        boolean expected = true;
        when(mockCollection.remove(someObject)).thenReturn(expected);
        boolean result = instance.remove(someObject);
        verify(mockCollection).remove(someObject);
        assertEquals(expected, result);
    }

    @Test
    public void containsAll() throws Exception {
        boolean expected = true;
        when(mockCollection.containsAll(anotherCollection)).thenReturn(expected);
        boolean result = instance.containsAll(anotherCollection);
        verify(mockCollection).containsAll(anotherCollection);
        assertEquals(expected, result);
    }

    @Test
    public void addAll() throws Exception {
        boolean expected = true;
        when(mockCollection.addAll(anotherCollection)).thenReturn(expected);
        boolean result = instance.addAll(anotherCollection);
        verify(mockCollection).addAll(anotherCollection);
        assertEquals(expected, result);
    }

    @Test
    public void removeAll() throws Exception {
        boolean expected = true;
        when(mockCollection.removeAll(anotherCollection)).thenReturn(expected);
        boolean result = instance.removeAll(anotherCollection);
        verify(mockCollection).removeAll(anotherCollection);
        assertEquals(expected, result);
    }

    @Test
    public void retainAll() throws Exception {
        boolean expected = true;
        when(mockCollection.retainAll(anotherCollection)).thenReturn(expected);
        boolean result = instance.retainAll(anotherCollection);
        verify(mockCollection).retainAll(anotherCollection);
        assertEquals(expected, result);
    }

    @Test
    public void clear() throws Exception {
        instance.clear();
        verify(mockCollection).clear();
    }

    @Test
    public void testToString() throws Exception {
        // Mockito can't verify() toString() calls, so we fall back to assertEquals()
        String expected = String.valueOf(Math.random());
        when(mockCollection.toString()).thenReturn(expected);
        String result = instance.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testHashCode() throws Exception {
        // Mockito can't verify(), or even stub, hashCode() calls,
        // so we fall back to assertEquals()
        int expected = mockCollection.hashCode();
        int result = instance.hashCode();
        assertEquals(expected, result);
    }

    @Test
    public void testEquals()  throws Exception {
        // Mockito can't verify(), or even stub, equals() calls

        // get a non-trivial collection...
        Collection<Object> someCollection = new ArrayList<>();
        someCollection.add(new Object());
        someCollection.add(new Object());
        someCollection.add(new Object());

        // decorate it
        CollectionDecorator<Object> instance = new CollectionDecorator<>(someCollection);
        assertTrue(instance.equals(someCollection));
        assertTrue(instance.equals(new ArrayList<>(someCollection)));
        assertFalse(instance.equals(new ArrayList<>()));
    }
}