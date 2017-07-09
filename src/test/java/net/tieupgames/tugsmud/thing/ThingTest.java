package net.tieupgames.tugsmud.thing;

import org.junit.Test;

import static org.junit.Assert.*;

public class ThingTest {

    private final int SOME_ID = 42;
    private final int SOME_OTHER_ID = 24;

    @Test
    public void getId() throws Exception {
        int expected = SOME_ID;
        int result = new Thing(expected).getId();
        assertEquals(expected, result);
    }

    @Test
    public void testHashCode() throws Exception {
        Thing instance = new Thing(SOME_ID);
        assertEquals(instance.getId(), instance.hashCode());
    }

    @Test
    public void equals() throws Exception {
        Thing instance = new Thing(SOME_ID);
        Thing equalInstance = new Thing(SOME_ID);
        assertTrue(instance.equals(equalInstance));

        Thing unequalInstance = new Thing(SOME_OTHER_ID);
        assertFalse(instance.equals(unequalInstance));

        Thing thingOfWrongClass = new Thing(SOME_ID) {
          // just a different class of thing...
        };
        try {
            instance.equals(thingOfWrongClass);
            fail("assert should have fired when different Thing classes have the same ID");
        } catch(AssertionError e) {}
        assertTrue(thingOfWrongClass.equals(thingOfWrongClass));

        assertFalse(instance.equals(new Object()));
        assertFalse(instance.equals(null));

    }

}