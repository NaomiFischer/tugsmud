package net.tieupgames.tugsmud.thing;

import net.tieupgames.tugsmud.parser.Identified;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class KindOfThingTest {

    private final KindOfThing instance = KindOfThing.get(Thing.class, false);

    private final class WeirdThing extends Thing {
        protected WeirdThing() throws ReflectiveOperationException {
            super(0, instance);
            throw new ReflectiveOperationException("dummy ROE");
        }
    }

    private static final class WeirdThing2 extends Thing {
        protected WeirdThing2(int id, KindOfThing kind) throws ReflectiveOperationException {
            super(id, kind);
            throw new ReflectiveOperationException("dummy ROE");
        }
    }

    @Test
    public void contains() throws Exception {
        Thing someThing = mock(Thing.class);

        // we contain things we contain...
        when(someThing.getKind()).thenReturn(instance);
        assertTrue(instance.contains(someThing));

        // ...and don't contain things we don't
        when(someThing.getKind()).thenReturn(null);
        assertFalse(instance.contains(someThing));

        verify(someThing, times(2)).getKind();
    }

    @Test
    public void getThing() throws Exception {
        int someThingId = Things.FIRST_ARBITRARY_ID;
        int anotherThingId = (Things.FIRST_ARBITRARY_ID * 2);

        // ensure getting a thing always returns the same thing per ID
        // ordinarily, I wouldn't want to test the caching behavior, but this is
        // such a common operation that it's worth it (but this can be safely removed if needed)
        Thing someThing = instance.getThing(someThingId);
        Thing theSameThing = instance.getThing(someThingId);
        assertTrue(someThing == theSameThing);

        // okay, now make sure we get the proper IDs back
        assertEquals(someThingId, someThing.getId());

        // make sure that we get different things per ID (probably superfluous given the above...)
        Thing anotherThing = instance.getThing(anotherThingId);
        assertFalse(someThing.equals(anotherThing));

        // this is probably overkill, but ensure we get expected behavior if the constructor asplodes
        try {
            KindOfThing.get(WeirdThing2.class, false).getThing(0);
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof ReflectiveOperationException);
        }
    }

    @Test
    public void get() throws Exception {
        // make sure we get Special kinds when we should
        KindOfThing someKind = KindOfThing.get(Thing.class, true);
        assertTrue(someKind instanceof Identified);
        assertEquals(KindOfThing.SPECIAL_KIND_ID, ((Identified)someKind).idForNewRegistryEntry());

        // and that we don't when we shouldn't
        assertFalse(instance instanceof Identified);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void constructorFailsFast() throws Exception {
        IllegalArgumentException onWrongClass = null, onMissingConstructor = null;

        // we should get an IllegalArgumentException if we try to make a Kind for a non-Thing class...
        try {
            Class<?> wrongClass = Object.class;
            KindOfThing.get((Class<Thing>)wrongClass, false);
        } catch (IllegalArgumentException e) {
            onWrongClass = e;
        }

        // ...or if our Thing class doesn't have the proper constructor
        try {
            KindOfThing.get(WeirdThing.class, false);
        } catch (IllegalArgumentException e) {
            onMissingConstructor = e;
        }

        assertNotNull("didn't fail when called with an inappropriate Class argument", onWrongClass);
        assertNotNull("didn't fail when called with a Class argument missing an int constructor", onMissingConstructor);
    }
}