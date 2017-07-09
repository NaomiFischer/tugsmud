package net.tieupgames.tugsmud.utils;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MapDecoratorTest {

    @SuppressWarnings("unchecked")
    private final Map<Object, Object> map = mock(Map.class);
    private final MapDecorator<Object, Object> instance = new MapDecorator<>(map);

    private final Object someObject = new Object();
    private final Map<Object, Object> anotherMap = new HashMap<>();

    @Test
    public void constructorRejectsNulls() {
        try {
            new MapDecorator<>(null);
            fail("constructor should have thrown a NullPointerException");
        } catch (NullPointerException e) {}
    }

    @Test
    public void size() throws Exception {
        int expected = (int)(Math.random() * 1000);
        when(map.size()).thenReturn(expected);
        int result = instance.size();
        verify(map).size();
        assertEquals(expected, result);
    }

    @Test
    public void isEmpty() throws Exception {
        boolean expected = true;
        when(map.isEmpty()).thenReturn(expected);
        boolean result = instance.isEmpty();
        verify(map).isEmpty();
        assertEquals(expected, result);
    }

    @Test
    public void containsKey() throws Exception {
        boolean expected = true;
        when(map.containsKey(someObject)).thenReturn(expected);
        boolean result = instance.containsKey(someObject);
        verify(map).containsKey(someObject);
        assertEquals(expected, result);
    }

    @Test
    public void containsValue() throws Exception {
        boolean expected = true;
        when(map.containsValue(someObject)).thenReturn(expected);
        boolean result = instance.containsValue(someObject);
        verify(map).containsValue(someObject);
        assertEquals(expected, result);
    }

    @Test
    public void get() throws Exception {
        Object expected = new Object();
        when(map.get(someObject)).thenReturn(expected);
        Object result = instance.get(someObject);
        verify(map).get(someObject);
        assertEquals(expected, result);
    }

    @Test
    public void put() throws Exception {
        Object expected = new Object();
        when(map.put(someObject, someObject)).thenReturn(expected);
        Object result = instance.put(someObject, someObject);
        verify(map).put(someObject, someObject);
        assertEquals(expected, result);
    }

    @Test
    public void remove() throws Exception {
        Object expected = new Object();
        when(map.remove(someObject)).thenReturn(expected);
        Object result = instance.remove(someObject);
        verify(map).remove(someObject);
        assertEquals(expected, result);
    }

    @Test
    public void putAll() throws Exception {
        instance.putAll(anotherMap);
        verify(map).putAll(anotherMap);
    }

    @Test
    public void clear() throws Exception {
        instance.clear();
        verify(map).clear();
    }

    @Test
    public void keySet() throws Exception {
        Set<Object> expected = new HashSet<>();
        expected.add(new Object()); // make it non-trivial
        when(map.keySet()).thenReturn(expected);
        Set<Object> result = instance.keySet();
        verify(map).keySet();
        assertEquals(expected, result);
    }

    @Test
    public void values() throws Exception {
        Collection<Object> expected = Arrays.asList(new Object());
        when(map.values()).thenReturn(expected);
        Collection<Object> result = instance.values();
        verify(map).values();
        assertEquals(expected, result);
    }

    @Test
    public void entrySet() throws Exception {
        Set<Map.Entry<Object, Object>> expected = new HashSet<>();
        expected.add(null); // make it non-trivial
        when(map.entrySet()).thenReturn(expected);
        Set<Map.Entry<Object, Object>> result = instance.entrySet();
        verify(map).entrySet();
        assertEquals(expected, result);
    }

    @Test
    public void testToString() throws Exception {
        String expected = String.valueOf(Math.random());
        when(map.toString()).thenReturn(expected);
        String result = instance.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testHashCode() throws Exception {
        int expected = map.hashCode();
        int result = instance.hashCode();
        assertEquals(expected, result);
    }

    @Test
    public void equals() throws Exception {
        // Mockito can't verify(), or even stub, equals() calls

        // get a non-trivial map...
        Map<Object, Object> someMap = new HashMap<>();
        someMap.put(new Object(), new Object());
        someMap.put(new Object(), new Object());
        someMap.put(new Object(), new Object());

        // decorate it
        MapDecorator<Object, Object> instance = new MapDecorator<>(someMap);
        assertTrue(instance.equals(someMap));
        assertTrue(instance.equals(new HashMap<>(someMap)));
        assertFalse(instance.equals(new HashMap<>()));
    }
}