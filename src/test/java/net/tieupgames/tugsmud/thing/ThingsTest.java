package net.tieupgames.tugsmud.thing;

import org.junit.Test;

import static org.junit.Assert.*;

public class ThingsTest {

    @Test
    public void extractKindId() throws Exception {
        int kindId = 42;
        int arbitraryId = (42 << Things.KIND_ID_BITS);
        int thingId = arbitraryId | kindId;
        int result = Things.extractKindId(thingId);
        assertEquals(kindId, result);
    }

    @Test
    public void extractSpecial() throws Exception {
        int someNonSpecialId = 42;
        int someSpecialId = -42;
        assertTrue(Things.extractSpecial(someNonSpecialId) == 0);
        assertFalse(Things.extractSpecial(someSpecialId) == 0);
    }

    @Test
    public void extractArbitraryId() throws Exception {
        int kindId = 42;
        int arbitraryId = (42 << Things.KIND_ID_BITS);
        int thingId = arbitraryId | kindId;
        int result = Things.extractArbitraryId(thingId);
        assertEquals(arbitraryId, result);
    }

    @Test
    public void getThing() {
        try {
            Things.getThing(0);
            fail("should have failed to find a kind ID");
        } catch (IndexOutOfBoundsException e) {}
    }
}