package net.tieupgames.tugsmud.thing;

import org.junit.Test;

import static org.junit.Assert.*;

public class KindOfThingTest {

    private final int kindId = 42;
    private final KindOfThing instance = new KindOfThing(Thing.class, kindId);

    private static final class WeirdThing extends Thing {
        protected WeirdThing() throws ReflectiveOperationException {
            super(0);
            throw new ReflectiveOperationException("dummy ROE");
        }
    }

    private static final class WeirdThing2 extends Thing {
        protected WeirdThing2(int id) throws ReflectiveOperationException {
            super(id);
            throw new ReflectiveOperationException("dummy ROE");
        }
    }

    @Test
    public void get() throws Exception {
        int someThingId = Things.FIRST_ARBITRARY_ID | kindId;
        int anotherThingId = (Things.FIRST_ARBITRARY_ID * 2) | kindId;

        // ensure getting a thing always returns the same thing per ID
        // ordinarily, I wouldn't want to test the caching behavior, but this is
        // such a common operation that it's worth it (but this can be safely removed if needed)
        Thing someThing = instance.get(someThingId);
        Thing theSameThing = instance.get(someThingId);
        assertTrue(someThing == theSameThing);

        // okay, now make sure we get the proper IDs back
        assertEquals(someThingId, someThing.getId());

        // make sure that we get different things per ID (probably superfluous given the above...)
        Thing anotherThing = instance.get(anotherThingId);
        assertFalse(someThing.equals(anotherThing));

        // this is probably overkill, but ensure we get expected behavior if the constructor asplodes
        try {
            new KindOfThing(WeirdThing2.class, 0).get(0);
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof ReflectiveOperationException);
        }
    }

    @Test
    public void contains() {
        int someThingId = instance.getId() | Things.FIRST_ARBITRARY_ID;
        int someOtherThingId = (instance.getId() * 2) | Things.FIRST_ARBITRARY_ID;
        assertTrue(instance.contains(someThingId));
        assertFalse(instance.contains(someOtherThingId));
        assertTrue(instance.contains(new Thing(someThingId)));
        assertFalse(instance.contains(new Thing(someOtherThingId)));
    }

    @Test
    public void getId() throws Exception {
        int result = instance.getId();
        assertEquals(kindId, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void constructorFailsFast() throws Exception {
        IllegalArgumentException onWrongClass = null, onMissingConstructor = null;

        // we should get an IllegalArgumentException if we try to make a Kind for a non-Thing class...
        try {
            Class<?> wrongClass = Object.class;
            new KindOfThing((Class<Thing>)wrongClass, 0);
        } catch (IllegalArgumentException e) {
            onWrongClass = e;
        }

        // ...or if our Thing class doesn't have the proper constructor
        try {
            new KindOfThing(WeirdThing.class, 0);
        } catch (IllegalArgumentException e) {
            onMissingConstructor = e;
        }

        assertNotNull("didn't fail when called with an inappropriate Class argument", onWrongClass);
        assertNotNull("didn't fail when called with a Class argument missing an int constructor", onMissingConstructor);
    }
}