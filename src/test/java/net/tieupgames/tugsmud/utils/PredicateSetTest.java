package net.tieupgames.tugsmud.utils;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PredicateSetTest {
    @Test
    public void view() throws Exception {
        Set<Object> set = new HashSet<>();
        set.add(new Object());
        set.add(new Object());
        set.add(new Object());
        PredicateSet<Object> instance = PredicateSet.view(set);
        assertTrue(instance.equals(set));
        assertTrue(set.equals(instance));
        Object object = new Object();
        set.add(object);
        assertTrue(instance.contains(object));
        instance.remove(object);
        assertFalse(set.contains(object));
    }

    @Test
    public void test() throws Exception {
        PredicateSet<Object> instance = new PredicateSet<>();
        Object object = new Object();
        instance.add(object);
        assertTrue(instance.test(object));
        assertFalse(instance.test(new Object()));
        assertFalse(instance.test(null));
    }

    @Test
    public void copyConstructor() {
        Set<Object> set = new HashSet<>();
        set.add(new Object());
        set.add(new Object());
        set.add(new Object());
        PredicateSet<Object> instance = new PredicateSet<>(set);
        assertTrue(set.equals(instance));
    }
}