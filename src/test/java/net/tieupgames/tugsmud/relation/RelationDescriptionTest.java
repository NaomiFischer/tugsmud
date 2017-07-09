package net.tieupgames.tugsmud.relation;

import net.tieupgames.tugsmud.parser.Registry;
import net.tieupgames.tugsmud.thing.KindOfThing;
import net.tieupgames.tugsmud.thing.Thing;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class RelationDescriptionTest {

    private final String someKindName = "Kind 1", someOtherKindName = "Kind 2";
    private final String someKey = "perseverance, the key to success";

    private final KindOfThing someKind = mock(KindOfThing.class);
    private final KindOfThing someOtherKind = mock(KindOfThing.class);

    private final Registry registry = mock(Registry.class);

    @Before
    @SuppressWarnings("unchecked") // mocking generics...
    public void setup() {
        when(someKind.contains(any(Thing.class))).thenAnswer( invocation -> {
            Thing thing = (Thing)invocation.getArguments()[0];
            return someKind.contains(thing.getId());
        });

        when(someOtherKind.contains(any(Thing.class))).thenAnswer( invocation -> {
            Thing thing = (Thing)invocation.getArguments()[0];
            return someOtherKind.contains(thing.getId());
        });

        when(registry.get(eq(someKindName), any(Class.class))).thenReturn(someKind);
        when(registry.get(eq(someOtherKindName), any(Class.class))).thenReturn(someOtherKind);
    }

    @Test
    public void get() throws Exception {
        RelationDescription instance = new RelationDescription("");
        instance.leftCardinality = Cardinality.MANY;
        instance.rightCardinality = Cardinality.ONE;

        @SuppressWarnings("unchecked") // mocking generics
        Predicate<Object> leftPredicate = mock(Predicate.class);
        when(leftPredicate.test(any())).thenReturn(true);
        @SuppressWarnings("unchecked") // mocking generics
        Predicate<Object> rightPredicate = mock(Predicate.class);
        when(rightPredicate.test(any())).thenReturn(true);
        instance.leftPredicate = leftPredicate;
        instance.rightPredicate = rightPredicate;

        @SuppressWarnings("unchecked")
        Relation<Object, Object> relation = instance.get();
        assertEquals(Cardinality.MANY, relation.leftCardinality());
        assertEquals(Cardinality.ONE, relation.rightCardinality());

        Object left = new Object();
        Object right = new Object();
        relation.insert(left, right);
        verify(instance.leftPredicate).test(left);
        verify(instance.rightPredicate).test(right);
    }

    @Test
    public void predicateFor() throws Exception {
        JSONObject json = new JSONObject();
        json.put(someKey, someKindName);
        RelationDescription instance = new RelationDescription("", registry);
        Predicate<Object> predicate = instance.predicateFor(json, someKey);

        // if our kind contains a thing, then it should match
        when(someKind.contains(anyInt())).thenReturn(true);
        assertTrue(predicate.test(new Thing(0)));

        // if it doesn't, it shouldn't
        when(someKind.contains(anyInt())).thenReturn(false);
        assertFalse(predicate.test(new Thing(0)));

        // lastly, non-Thing objects should always fail
        when(someKind.contains(anyInt())).thenReturn(true);
        assertFalse(predicate.test(new Object()));

        assertEquals(someKindName, predicate.toString());
    }

    @Test
    public void defaultPredicates() {
        RelationDescription instance = new RelationDescription("");
        Predicate<Object> leftPredicate = instance.leftPredicate;
        Predicate<Object> rightPredicate = instance.rightPredicate;
        assertTrue(leftPredicate.test(new Object()));
        assertTrue(rightPredicate.test(new Object()));
    }
}