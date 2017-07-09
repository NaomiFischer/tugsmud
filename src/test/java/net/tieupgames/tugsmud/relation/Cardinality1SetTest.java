package net.tieupgames.tugsmud.relation;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class Cardinality1SetTest {

    @Test
    public void size() throws Exception {
        Cardinality1Set<Object> instance = new Cardinality1Set<>();
        assertEquals(0, instance.size());
        instance.add(new Object());
        assertEquals(1, instance.size());
        instance.add(new Object());
        assertEquals(1, instance.size());
    }

    @Test
    public void isEmpty() throws Exception {
        Cardinality1Set<Object> instance = new Cardinality1Set<>();
        assertTrue(instance.isEmpty());
        instance.add(new Object());
        assertFalse(instance.isEmpty());
    }

    @Test
    public void contains() throws Exception {
        Object object = new Object();
        Cardinality1Set<Object> instance = new Cardinality1Set<>();
        assertFalse(instance.contains(object));
        instance.add(object);
        assertTrue(instance.contains(object));
        instance.add(new Object());
        assertFalse(instance.contains(object));
    }

    @Test
    public void iterator() throws Exception {
        Object object = new Object();
        Cardinality1Set<Object> instance = new Cardinality1Set<>();
        instance.add(new Object());
        instance.add(object);
        for (Object o : instance) {
            assertEquals(object, o);
        }
        Iterator<Object> iterator = instance.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(object, iterator.next());
        assertFalse(iterator.hasNext());
        try {
            iterator.next();
            fail("iterator should have thrown a NoSuchElementException");
        } catch (NoSuchElementException e) {}

        iterator = instance.iterator();
        try {
            iterator.remove();
            fail("iterator should have thrown an IllegalStateException");
        } catch (IllegalStateException e) {}

        iterator.next();
        iterator.remove();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void iteratorFailFastBehavior() {
        // the iterators returned by collections are supposed to fail with a ConcurrentModificationException
        // if next() or remove() is called and the collection has been modified since the iterator was created
        Cardinality1Set<Object> instance = new Cardinality1Set<>();
        instance.add(new Object());
        Iterator<Object> iterator = instance.iterator();

        // illegal modification of our set!
        instance.add(new Object());

        try {
            iterator.next();
            fail("iterator should have thrown a ConcurrentModificationException");
        } catch(ConcurrentModificationException e) {}

        // now the same thing with remove
        iterator = instance.iterator();
        iterator.next();
        instance.add(new Object());
        try {
            iterator.remove();
            fail("iterator should have thrown a ConcurrentModificationException");
        } catch(ConcurrentModificationException e) {}
    }

    @Test
    public void toArray() throws Exception {
        Object object = new Object();
        Object[] expected = new Object[]{object};
        Cardinality1Set<Object> instance = new Cardinality1Set<>();
        instance.add(object);
        Object[] result = instance.toArray();
        assertArrayEquals(expected, result);
    }

    @Test
    public void toArray1() throws Exception {
        Object object = new Object();
        Object[] expected = new Object[]{object};
        Cardinality1Set<Object> instance = new Cardinality1Set<>();
        instance.add(object);
        Object[] result = instance.toArray(new Object[0]);
        assertArrayEquals(expected, result);
    }

    @Test
    public void add() throws Exception {
        Object object = new Object();
        Cardinality1Set<Object> instance = new Cardinality1Set<>();
        assertTrue(instance.add(new Object()));
        assertTrue(instance.add(object));
        assertFalse(instance.add(object));
        assertTrue(instance.contains(object));

        instance = new Cardinality1Set<>();
        assertTrue(instance.add(null));
        assertFalse(instance.isEmpty());
        assertFalse(instance.add(null));
    }

    @Test
    public void remove() throws Exception {
        Object object = new Object();
        Cardinality1Set<Object> instance = new Cardinality1Set<>();
        assertFalse(instance.remove(object));
        instance.add(new Object());
        assertFalse(instance.remove(object));
        instance.add(object);
        assertTrue(instance.remove(object));
        assertTrue(instance.isEmpty());

        instance = new Cardinality1Set<>();
        assertFalse(instance.remove(null));
        instance.add(new Object());
        assertFalse(instance.remove(null));
        instance.add(null);
        assertTrue(instance.remove(null));
        assertTrue(instance.isEmpty());
    }

    @Test
    public void containsAll() throws Exception {
        List<Object> objects = new ArrayList<>();
        Cardinality1Set<Object> instance = new Cardinality1Set<>();
        assertTrue(instance.containsAll(objects));
        Object object = new Object();
        objects.add(object);
        assertFalse(instance.containsAll(objects));
        instance.add(object);
        assertTrue(instance.containsAll(objects));
        objects.add(new Object());
        assertFalse(instance.containsAll(objects));
    }

    @Test
    public void addAll() throws Exception {
        try {
            Cardinality1Set<Object> instance = new Cardinality1Set<>();
            instance.addAll(new ArrayList<>());
            fail("addAll should have thrown an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {}
    }

    @Test
    public void retainAll() throws Exception {
        Cardinality1Set<Object> instance = new Cardinality1Set<>();
        Object object = new Object();
        Set<Object> otherSet = new HashSet<>();

        // retainAll on an empty set is always a no-op
        assertFalse(instance.retainAll(Arrays.asList(otherSet)));

        // retainAll is also a no-op if the other collection contains this one's element
        instance.add(object);
        otherSet.add(object);
        assertFalse(instance.retainAll(otherSet));
        assertTrue(instance.contains(object));

        // otherwise, we end up clearing the set
        instance.add(object);
        otherSet.clear();
        otherSet.add(new Object());
        assertTrue(instance.retainAll(otherSet));
        assertTrue(instance.isEmpty());

    }

    @Test
    public void removeAll() throws Exception {
        Cardinality1Set<Object> instance = new Cardinality1Set<>();
        Object object = new Object();
        List<Object> list = new ArrayList<>();
        assertFalse(instance.removeAll(list));
        list.add(object);
        assertFalse(instance.removeAll(list));
        instance.add(object);
        assertTrue(instance.removeAll(list));
        assertTrue(instance.isEmpty());
        instance.add(new Object());
        assertFalse(instance.removeAll(list));
    }

    @Test
    public void clear() throws Exception {
        Cardinality1Set<Object> instance = new Cardinality1Set<>();
        instance.add(new Object());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void equals() throws Exception {
        Cardinality1Set<Object> instance = new Cardinality1Set<>();
        Set<Object> otherSet = new HashSet<>();
        Object object = new Object();
        assertTrue(instance.equals(otherSet));
        otherSet.add(object);
        assertFalse(instance.equals(otherSet));
        otherSet.clear();
        instance.add(object);
        assertFalse(instance.equals(otherSet));
        otherSet.add(object);
        assertTrue(instance.equals(otherSet));
        otherSet.add(new Object());
        assertFalse(instance.equals(otherSet));
        assertFalse("equals() should ensure the other object is a set", instance.equals(Collections.singleton(object)));
        assertFalse("equals() should reject nulls", instance.equals(null));
    }

    @Test
    public void testHashCode() throws Exception {
        // hash code is 0 if empty
        Cardinality1Set<Object> instance = new Cardinality1Set<>();
        assertEquals(0, instance.hashCode());

        // and the hash code of the contents otherwise
        Object o = new Object();
        instance.add(o);
        assertEquals(o.hashCode(), instance.hashCode());

        // if our element is null, the hash code should still be 0
        instance = new Cardinality1Set<>();
        instance.add(null);
        assertEquals(Objects.hashCode(null), instance.hashCode());

    }
}
