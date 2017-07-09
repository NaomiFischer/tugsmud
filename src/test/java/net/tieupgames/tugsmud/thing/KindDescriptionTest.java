package net.tieupgames.tugsmud.thing;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class KindDescriptionTest {

    private final String someKey = "the sword is a key, and when you stab people with it, it unlocks their death";
    private final String someName = "Her Kinky Majesty Naomi XLII";
    private final int someKindId = 0x69;

    @Test
    public void constructor1() {
        assertEquals(KindFactory.INSTANCE, new KindDescription(someName).factory);
    }

    @Test
    public void get() throws Exception {
        KindFactory mockFactory = mock(KindFactory.class);
        KindDescription instance = new KindDescription(someName, mockFactory);
        instance.get();
        verify(mockFactory).createKind(Thing.class);

        // make sure the special kind is handled properly
        instance.special = true;
        instance.get();
        verify(mockFactory).createSpecialKind(Thing.class);
    }
}