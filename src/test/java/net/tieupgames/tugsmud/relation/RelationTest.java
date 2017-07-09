package net.tieupgames.tugsmud.relation;

import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RelationTest {

    private Set<Object> mockCurryLeft = mock(Set.class);
    private Set<Map.Entry<Object, Object>> mockEntrySet = mock(Set.class);

    private Relation<Object, Object> instance = new Relation<Object, Object>() {

        @Override
        public Cardinality leftCardinality() {
            throw new UnsupportedOperationException("test implementation doesn't support this method");
        }

        @Override
        public Cardinality rightCardinality() {
            throw new UnsupportedOperationException("test implementation doesn't support this method");
        }

        @Override
        public Set<Object> curryLeft(Object key) {
            return mockCurryLeft;
        }

        @Override
        public Set<Object> curryRight(Object key) {
            throw new UnsupportedOperationException("test implementation doesn't support this method");
        }

        @Override
        public void insert(Object key, Object value) {
            throw new UnsupportedOperationException("test implementation doesn't support this method");
        }

        @Override
        public void remove(Object key, Object value) {
            throw new UnsupportedOperationException("test implementation doesn't support this method");
        }
    };

    @Test
    public void contains() throws Exception {
        Object left = new Object();
        Object right = new Object();

        // we shouldn't contain any particular entry at first...
        assertFalse(instance.contains(left, right));

        // but once we insert it, we should
        when(mockCurryLeft.contains(right)).thenReturn(true);
        assertTrue(instance.contains(left, right));

        // we should still return false for other entries
        assertFalse(instance.contains(left, new Object()));
    }
}