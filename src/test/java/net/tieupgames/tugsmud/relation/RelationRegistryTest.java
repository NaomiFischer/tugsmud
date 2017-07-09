package net.tieupgames.tugsmud.relation;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class RelationRegistryTest {

    private final List<Relation<?, ?>> idsToRelation = new ArrayList<>();
    private final Map<Relation<?, ?>, Integer> relationsToId = new HashMap<>();
    private final RelationRegistry instance = new RelationRegistry(idsToRelation, relationsToId);

    @Test
    public void getId() throws Exception {
        Relation<?, ?> someRelation = mock(Relation.class);
        int expected = 0;
        int result = instance.getId(someRelation);
        assertEquals(expected, result);
        assertEquals(expected, (int)relationsToId.get(someRelation));
        assertTrue(idsToRelation.contains(someRelation));
        assertTrue(relationsToId.containsKey(someRelation));

        // now make sure we get the same thing back
        Relation<?, ?> someOtherRelation = mock(Relation.class);
        int otherResult = instance.getId(someOtherRelation);
        assertFalse(result == otherResult);
        int sameResult = instance.getId(someRelation);
        assertEquals(expected, sameResult);
    }

    @Test
    public void getRelation() throws Exception {
        // ensure we get the expected exception on non-present IDs
        try {
            instance.getRelation(0);
            fail("should have failed with an IndexOutOfBoundsException on unknown ID");
        } catch (IndexOutOfBoundsException e) {}

        // insert a relation...
        Relation<?, ?> someRelation = mock(Relation.class);
        idsToRelation.add(someRelation);
        relationsToId.put(someRelation, 0);
        assertEquals(someRelation, instance.getRelation(0));
    }

}