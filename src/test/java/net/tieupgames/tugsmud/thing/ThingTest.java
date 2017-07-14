package net.tieupgames.tugsmud.thing;

import net.tieupgames.tugsmud.parser.Registry;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ThingTest {

    private final int someId = 42;
    private final int someOtherId = 24;

    private final KindOfThing someKind = KindOfThing.get(Thing.class, false);

    @Test
    public void getId() throws Exception {
        int expected = someId;
        int result = new Thing(expected, someKind).getId();
        assertEquals(expected, result);
    }

    @Test
    public void constructorIntRegistry() throws Exception {
        Registry mockRegistry = mock(Registry.class);
        KindOfThing mockSpecialKind = mock(KindOfThing.class);
        when(mockRegistry.get(someId, KindOfThing.class)).thenReturn(someKind);
        when(mockRegistry.get(KindOfThing.SPECIAL_KIND_ID, KindOfThing.class)).thenReturn(mockSpecialKind);
        assertEquals(someKind, new Thing(someId, mockRegistry).getKind());
        assertEquals(mockSpecialKind, new Thing(KindOfThing.SPECIAL_KIND_ID | someOtherId, mockRegistry).getKind());
    }

    @Test
    public void getKind() throws Exception {
        assertEquals(someKind, new Thing(someId, someKind).getKind());
    }

    @Test
    public void testHashCode() throws Exception {
        Thing instance = new Thing(someId, someKind);
        assertEquals(instance.getId(), instance.hashCode());
    }

    @Test
    public void equals() throws Exception {
        Thing instance = new Thing(someId, someKind);
        Thing equalInstance = new Thing(someId, someKind);
        assertTrue(instance.equals(equalInstance));

        Thing unequalInstance = new Thing(someOtherId, someKind);
        assertFalse(instance.equals(unequalInstance));

        Thing thingOfWrongClass = new Thing(someId, someKind) {
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

    @Test
    public void idForNewRegistryEntry() {
        assertEquals(someId, new Thing(someId, someKind).idForNewRegistryEntry());
    }
}