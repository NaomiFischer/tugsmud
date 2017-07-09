package net.tieupgames.tugsmud.thing;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class ThingsImplTest {

    private final Thing mockThing = mock(Thing.class);
    private final Thing mockSpecialThing = mock(Thing.class);
    private final KindOfThing mockKind = mock(KindOfThing.class);
    private final KindOfThing mockSpecialKind = mock(KindOfThing.class);
    private final KindFactory mockKindFactory = mock(KindFactory.class);

    private final int someKindId = 42;
    private final int someThingId = Things.FIRST_ARBITRARY_ID | someKindId;
    private final int someSpecialId = Things.FIRST_SPECIAL_ID | someThingId;

    private final ThingsImpl instance = new ThingsImpl(mockKindFactory);

    @Before
    public void setup() {
        when(mockKindFactory.getKind(someKindId)).thenReturn(mockKind);
        when(mockKindFactory.getSpecialKind()).thenReturn(mockSpecialKind);
        when(mockKind.get(anyInt())).thenReturn(mockThing);
        when(mockSpecialKind.get(anyInt())).thenReturn(mockThing);
    }

    @Test
    public void getThing() throws Exception {
        Thing gotThing = instance.getThing(someThingId);
        assertEquals(mockThing, gotThing);

        // ensure we use the factory appropriately
        // normal Things should use getKind()...
        verify(mockKindFactory).getKind(someKindId);
        verify(mockKindFactory, times(1)).getKind(anyInt());
        verify(mockKindFactory.getKind(someKindId)).get(someThingId);

        // ...and special Things should use getSpecialKind()
        Thing gotSpecialThing = instance.getThing(someSpecialId);
        assertEquals(mockSpecialThing, gotSpecialThing);
        verify(mockKindFactory).getSpecialKind();
        verify(mockKindFactory.getSpecialKind()).get(someSpecialId);
    }
}