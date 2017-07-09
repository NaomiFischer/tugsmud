package net.tieupgames.tugsmud.relation;

import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RelationWithPredicatesTest {

    @Test
    public void insert() throws Exception {
        for (boolean mockLeftPasses: new boolean[]{true, false}) {
            Predicate<Object> mockLeftPredicate = anything -> mockLeftPasses;
            for (boolean mockRightPasses: new boolean[]{true, false}) {
                Predicate<Object> mockRightPredicate = anything -> mockRightPasses;

                IllegalArgumentException e = null;
                try {
                    Relation<Object, Object> instance = new RelationWithPredicates<>(
                            Cardinality.MANY, Cardinality.MANY, mockLeftPredicate, mockRightPredicate);
                    instance.insert(new Object(), new Object());
                } catch (IllegalArgumentException e2) {
                    e = e2;
                }
                boolean shouldHaveFailed = !mockLeftPasses || !mockRightPasses;
                if (shouldHaveFailed) {
                    assertNotNull(e);
                } else {
                    assertNull(e);
                }
            }}
    }
}