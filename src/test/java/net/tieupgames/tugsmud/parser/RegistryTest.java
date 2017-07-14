package net.tieupgames.tugsmud.parser;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegistryTest {

    private static final String someString = "there is no string";
    private static final Object someObject = "there is only zuul";

    private static final String someOtherString = "there is actually a string";
    private static final Object someOtherObject = new Object();

    private final Map<String, Object> entries = new HashMap<>();
    private final Map<Class<?>, EntryIDTracker> idTrackers = new HashMap<>();

    private final Registry instance = new Registry(entries, idTrackers);

    @Test
    public void getStringClass() throws Exception {
        entries.put(someString, someObject);
        entries.put(someOtherString, someOtherObject);

        // ensure that we can find registered values
        assertEquals(someObject, instance.get(someString, someObject.getClass()));
        assertEquals(someOtherObject, instance.get(someOtherString, someOtherObject.getClass()));

        // and that we don't find values we don't have
        Object result = instance.get("this isn't present", someObject.getClass());
        assertEquals(null, result);
        try {
            instance.get(someString, Void.class);
            fail("lookup should have failed with a ClassCastException");
        } catch (ClassCastException e) {}

        // ensure we throw out nulls
        try {
            instance.get(null, someObject.getClass());
            fail("should have thrown a NullPointerException");
        } catch(NullPointerException e) {}
        try {
            instance.get(someString, null);
            fail("should have thrown a NullPointerException");
        } catch(NullPointerException e) {}
    }

    @Test
    public void getIntClass() throws Exception {
        int key = 80085;
        EntryIDTracker tracker = mock(EntryIDTracker.class);
        when(tracker.get(key)).thenReturn(someObject);
        idTrackers.put(someObject.getClass(), tracker);
        Object result = instance.get(key, someObject.getClass());
        assertEquals(someObject, result);
        verify(tracker).get(key);
    }

    @Test
    public void getIdFor() throws Exception {
        int key = 80085;
        EntryIDTracker tracker = mock(EntryIDTracker.class);
        when(tracker.getId(someObject)).thenReturn(key);
        idTrackers.put(Object.class, tracker);
        int result = instance.getIdFor(someObject, Object.class);
        assertEquals(result, key);
    }

    @Test
    public void add() throws Exception {
        instance.add(someString, someObject, someObject.getClass());

        assertEquals(someObject, entries.get(someString));
        assertEquals("too many values were registered", 1, entries.size());

        // ensure objects are written to the entry tracker
        assertTrue(idTrackers.containsKey(someObject.getClass()));
        assertEquals(someObject, idTrackers.get(someObject.getClass()).get(0));

        // ensure we get a ParseException on duplicate entries
        try {
            instance.add(someString, someObject, someObject.getClass());
            fail("should have gotten a ParseException on duplicate entries");
        } catch (ParseException e) {
            assertEquals(ParseException.Reason.DUPLICATE_ENTRY, e.getReason());
        }

        // ensure we throw out nulls
        try {
            instance.add(null, someObject, someObject.getClass());
            fail("should have thrown a NullPointerException");
        } catch(NullPointerException e) {}
        try {
            instance.add(someString, null, someObject.getClass());
            fail("should have thrown a NullPointerException");
        } catch(NullPointerException e) {}
    }

    @Test
    public void testToString() throws Exception {
        @SuppressWarnings("unchecked") // mocking generics
        Map<String, Object> mockMap = mock(Map.class);
        when(mockMap.toString()).thenReturn(someString);
        String result = new Registry(mockMap, Collections.emptyMap()).toString();
        assertEquals(someString, result);
    }

    @Test
    public void defaultConstructor() throws Exception {
        Registry instance = new Registry();
        instance.add(someString, someObject, someObject.getClass());
        Object result = instance.get(someString, someObject.getClass());
        assertEquals(someObject, result);
    }
}