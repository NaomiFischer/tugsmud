package net.tieupgames.tugsmud.thing;

import org.junit.Test;

import static org.junit.Assert.*;

public class KindFactoryTest {

    // we use this instance instead of KindFactory.INSTANCE so our tests don't have any side-effects between tests
    private final KindFactory instance = new KindFactory();

    private static final class SecretThing extends Thing {
        SecretThing(int id) {
            super(id);
        }
    }

    @Test
    public void createKind() throws Exception {
        KindOfThing someKind = instance.createKind(SecretThing.class);
        assertEquals(0, someKind.getId());

        // ensure that our created Kind returns the right Thing class
        Thing thing = someKind.get(0);
        assertEquals(SecretThing.class, thing.getClass());
    }

    @Test
    public void getKind() throws Exception {
        try {
            instance.getKind(0);
            fail("getKind should have failed on an unknown ID");
        } catch (IndexOutOfBoundsException e) {}

        KindOfThing someKind = instance.createKind(Thing.class);
        KindOfThing theSameKind = instance.getKind(someKind.getId());
        assertTrue(someKind.equals(theSameKind));

        // ensure we can get the special kind properly
        KindOfThing specialKind = instance.createSpecialKind(Thing.class);
        assertEquals(specialKind, instance.getKind(KindFactory.SPECIAL_ID));
    }

    @Test
    public void createSpecialKind() throws Exception {
        KindOfThing kind = instance.createSpecialKind(Thing.class);
        assertEquals(kind, instance.getSpecialKind());
        try {
            instance.createSpecialKind(Thing.class);
            fail("should have failed with an IllegalStateException on multiple special kinds");
        } catch (IllegalStateException e) {}
    }

    @Test
    public void getSpecialKind() throws Exception {
        try {
            instance.getSpecialKind();
            fail("should have failed with an IllegalStateException, since no special kind is registered");
        } catch (IllegalStateException e) {}
    }
}