package net.tieupgames.tugsmud.relation;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class BaseRelationTest {

    private final Map<Object, Set<Object>> left = new HashMap<>();
    private final Map<Object, Set<Object>> right = new HashMap<>();

    // some test keys
    private final Object key1 = new Object();
    private final Object key2 = new Object();
    // and test values
    private final Object value1 = new Object();
    private final Object value2 = new Object();
    private final Object valueBoth = new Object();

    // from those, we can make a test instance!
    private final BaseRelation<Object, Object> instance = new BaseRelation<>(Cardinality.MANY, Cardinality.MANY, left, right);

    @Before
    public void setup() {
        // we insert our entries manually to avoid making tests depend on insert() that shouldn't
        left.put(key1, set(value1, valueBoth));
        left.put(key2, set(value2, valueBoth));
        right.put(value1, set(key1));
        right.put(value2, set(key2));
        right.put(valueBoth, set(key1, key2));
    }

    @Test
    public void leftCardinality() throws Exception {
        for (Cardinality expected: Cardinality.values()) {
            assertEquals(expected, new BaseRelation(expected, expected).leftCardinality());
        }
    }

    @Test
    public void rightCardinality() throws Exception {
        for (Cardinality expected: Cardinality.values()) {
            assertEquals(expected, new BaseRelation(expected, expected).rightCardinality());
        }
    }

    @Test
    public void curryLeft() throws Exception {
        // ensure we see the right values
        Set<Object> expected = set(value1, valueBoth);
        Set<Object> result = instance.curryLeft(key1);
        assertEquals(expected, result);

        // the sets returned by curryLeft are supposed to be unmodifiable
        try {
            result.add(new Object());
            fail("set returned by curryLeft didn't reject modification");
        } catch(UnsupportedOperationException e) {}

        // rerun the above test, but with a non-present key
        try {
            instance.curryLeft(new Object()).add(new Object());
            fail("(empty) set returned by curryLeft didn't reject modification");
        } catch(UnsupportedOperationException e) {}
    }

    @Test
    public void curryRight() throws Exception {
        // ensure we see the right values
        Set<Object> expected = set(key1, key2);
        Set<Object> result = instance.curryRight(valueBoth);
        assertEquals(expected, result);

        // the sets returned by curryLeft are supposed to be unmodifiable
        try {
            result.add(new Object());
            fail("set returned by curryRight didn't reject modification");
        } catch(UnsupportedOperationException e) {}

        // rerun the above test, but with a non-present key
        try {
            instance.curryRight(new Object()).add(new Object());
            fail("(empty) set returned by curryRight didn't reject modification");
        } catch(UnsupportedOperationException e) {}
    }

    @Test
    public void insert() throws Exception {
        // we inject our own maps so we can avoid calling curryLeft/curryRight
        // and we can't use the normal test init code because that doesn't call insert()
        BaseRelation<Object, Object> instance = new BaseRelation<>(Cardinality.MANY, Cardinality.MANY, left, right);
        // key1 -> value1, key2 -> value2, either key -> valueBoth
        instance.insert(key1, value1);
        instance.insert(key2, value2);
        instance.insert(key1, valueBoth);
        instance.insert(key2, valueBoth);

        // add some other entries to ensure sets don't "interfere" with each other
        instance.insert(new Object(), new Object());
        instance.insert(new Object(), new Object());
        instance.insert(new Object(), new Object());

        // check that those mappings are all correct - we should see all, and only, the expected values
        assertEquals(set(value1, valueBoth), left.get(key1));
        assertEquals(set(value2, valueBoth), left.get(key2));

        // and make sure they're correct from the other side, as well
        assertEquals(set(key1), right.get(value1));
        assertEquals(set(key2), right.get(value2));
        assertEquals(set(key1, key2), right.get(valueBoth));
    }

    @Test
    public void remove() throws Exception {
        // remove the key1 -> value1 entry
        instance.remove(key1, value1);

        // ensure it was actually removed
        assertTrue("left-hand mapping retained removed value", !left.get(key1).contains(value1));
        assertTrue("right-hand mapping retained removed value", !right.get(value1).contains(key1));

        // ensure other mappings weren't touched
        assertTrue("removing an entry had side-effects on other entries", left.get(key1).contains(valueBoth));
        assertTrue("removing an entry had side-effects on other entries", right.get(valueBoth).contains(key1));
        assertTrue("removing an entry had side-effects on other entries", right.get(valueBoth).contains(key2));
        assertTrue("removing an entry had side-effects on unrelated entries", left.get(key2).contains(value2));
        assertTrue("removing an entry had side-effects on unrelated entries", right.get(value2).contains(key2));
    }

    private static Set<Object> set(Object... elements) {
        return new HashSet<>(Arrays.asList(elements));
    }
}