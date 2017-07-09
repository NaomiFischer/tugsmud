package net.tieupgames.tugsmud.relation;

import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class CardinalityTest {
    @Test
    public void createSet() throws Exception {
        Object key = "potato";
        Object value = new Object();
        Map<Object, Set<Object>> map = new HashMap<>();
        Set<Object> set = Cardinality.MANY.createSet(key, map);
        map.put(key, set);
        set.add(value);

        // sanity check
        assertTrue(map.containsKey(key));
        assertEquals(set, map.get(key));
        assertTrue(map.get(key).contains(value));

        // removing the last key via remove() should remove the set
        set.remove(value);
        assertFalse(map.containsKey(key));

        set.add(value);
        map.put(key, set);

        // likewise with clear()
        assertTrue(map.containsKey(key));
        set.clear();
        assertFalse(map.containsKey(key));

        set.add(value);
        map.put(key, set);

        // and removeAll()
        assertTrue(map.containsKey(key));
        set.removeAll(set);
        assertFalse(map.containsKey(key));

        set.add(value);
        map.put(key, set);

        // and the iterator
        assertTrue(map.containsKey(key));
        Iterator<Object> iterator = set.iterator();
        iterator.next();
        iterator.remove();
        assertFalse(map.containsKey(key));

        // removing values shouldn't do anything if there are more, however
        set.add(value);
        set.add(new Object());
        map.put(key, set);
        assertTrue(map.containsKey(key));
        set.remove(value);
        assertTrue(map.containsKey(key));
    }

    @Test
    public void createSetImpl() throws Exception {
        assertTrue(Cardinality.ONE.createSetImpl() instanceof Cardinality1Set);
        assertFalse(Cardinality.MANY.createSetImpl() instanceof Cardinality1Set);
    }

}