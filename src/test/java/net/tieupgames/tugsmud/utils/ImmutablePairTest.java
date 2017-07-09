package net.tieupgames.tugsmud.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ImmutablePairTest {

    private final Object leftObject = new Object();
    private final Object rightObject = new Object();
    private final ImmutablePair<Object, Object> instance = new ImmutablePair<>(leftObject, rightObject);

    @Test
    public void getLeft() throws Exception {
        assertEquals(leftObject, instance.getLeft());
    }

    @Test
    public void getRight() throws Exception {
        assertEquals(rightObject, instance.getRight());
    }

    @Test
    public void equals() throws Exception {

        assertTrue("ImmutablePair wasn't equal to another ImmutablePair containing the same objects",
                instance.equals(new ImmutablePair<>(leftObject, rightObject)));

        Map<Object, Object> map = new HashMap<>();
        map.put(leftObject, rightObject);
        Map.Entry<Object, Object> someOtherPair = map.entrySet().iterator().next();

        assertTrue("ImmutablePair wasn't equal to a (non-ImmutablePair) Entry containing the same objects",
                instance.equals(someOtherPair));

        assertFalse(instance.equals(new Object()));
        assertFalse(instance.equals(null));

    }

    @Test
    public void testHashCode() throws Exception {
        // use a canonical Map.Entry implementation to ensure we're computing the hash codes correctly
        Map<Object, Object> map = new HashMap<>();
        map.put(leftObject, rightObject);
        Map.Entry<Object, Object> someOtherPair = map.entrySet().iterator().next();
        int expected = someOtherPair.hashCode();
        int result = instance.hashCode();
        assertEquals(expected, result);
    }

    @Test
    public void getKey() throws Exception {
        assertEquals(instance.getLeft(), instance.getKey());
    }

    @Test
    public void getValue() throws Exception {
        assertEquals(instance.getRight(), instance.getValue());
    }

    @Test
    public void setValue() throws Exception {
        try {
            instance.setValue(new Object());
            fail("ImmutablePair should have refused to change value with an UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {}
    }

    @Test
    public void testToString() {
        String expected = "(" + leftObject + ", " + rightObject + ")";
        String result = instance.toString();
        assertEquals(expected, result);
    }
}