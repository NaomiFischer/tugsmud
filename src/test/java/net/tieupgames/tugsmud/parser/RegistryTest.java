package net.tieupgames.tugsmud.parser;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegistryTest {

    private static final String someString = "there is no string";
    private static final Object someObject = "there is only zuul";

    private static final String someOtherString = "there is actually a string";
    private static final Object someOtherObject = new Object();

    @Test
    public void get() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put(someString, someObject);
        map.put(someOtherString, someOtherObject);
        Registry instance = new Registry(map);

        // ensure that we can find registered values
        assertEquals(someObject, instance.get(someString, someObject.getClass()));
        assertEquals(someOtherObject, instance.get(someOtherString, someOtherObject.getClass()));

        // and that we don't find values we don't have
        try {
            instance.get("this isn't present", someObject.getClass());
            fail("lookup should have failed with a NullPointerException");
        } catch (NullPointerException e) {}
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
    public void add() throws Exception {
        Map<String, Object> map = new HashMap<>();
        Registry instance = new Registry(map);
        instance.add(someString, someObject);

        assertEquals(someObject, map.get(someString));
        assertEquals("too many values were registered",1, map.size());

        // ensure we get a ParseException on duplicate entries
        try {
            instance.add(someString, someObject);
            fail("should have gotten a ParseException on duplicate entries");
        } catch (ParseException e) {
            assertEquals(ParseException.Reason.DUPLICATE_ENTRY, e.getReason());
        }

        // ensure we throw out nulls
        try {
            instance.add(null, someObject.getClass());
            fail("should have thrown a NullPointerException");
        } catch(NullPointerException e) {}
        try {
            instance.add(someString, null);
            fail("should have thrown a NullPointerException");
        } catch(NullPointerException e) {}
    }

    @Test
    public void testToString() {
        @SuppressWarnings("unchecked")
        Map<String, Object> mockMap = mock(Map.class);
        when(mockMap.toString()).thenReturn(someString);
        String result = new Registry(mockMap).toString();
        assertEquals(someString, result);
    }

    @Test
    public void lookup() throws Exception {
        Registry.INSTANCE.add(someString, someObject);
        Registry.INSTANCE.get(someString, someObject.getClass());
    }
}