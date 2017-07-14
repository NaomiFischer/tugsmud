package net.tieupgames.tugsmud.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class EntryIDTrackerTest {

    private final EntryIDTracker instance = new EntryIDTracker();
    private static final int someId = 0xBA116A6;
    private static final Identified someIdentified = new Identified() {
        @Override public int idForNewRegistryEntry() {
            return someId;
        }
    };

    @Test
    public void getId() throws Exception {
        instance.add(someIdentified);
        int result = instance.getId(someIdentified);
        assertEquals(someId, result);
    }

    @Test
    public void addAndGet() throws Exception {
        // by default, we add in sequential order
        Object object0 = new Object();
        Object object1 = new Object();
        instance.add(object0);
        instance.add(object1);
        assertEquals(object0, instance.get(0));
        assertEquals(object1, instance.get(1));

        // make sure Identified instances get the proper ID
        instance.add(someIdentified);
        assertEquals(someIdentified, instance.get(someId));

        // and make sure we get a NPE on a missing object
        NullPointerException expectedException = null;
        try {
            instance.get(2);
        } catch(NullPointerException e) {
            expectedException = e;
        }
        assertNotNull("should have failed with a NullPointerException", expectedException);
    }
}