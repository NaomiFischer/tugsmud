package net.tieupgames.tugsmud.relation;

import java.util.*;

public class BaseRelation<L, R> implements Relation<L, R> {

    private final Cardinality _leftCardinality, _rightCardinality;
    private final Map<L, Set<R>> _map;
    private final Map<R, Set<L>> _reversedMap;

    BaseRelation(Cardinality leftCardinality, Cardinality rightCardinality) {
        this(leftCardinality, rightCardinality, new HashMap<>(), new HashMap<>());
    }

    BaseRelation(Cardinality leftCardinality, Cardinality rightCardinality, Map<L, Set<R>> map, Map<R, Set<L>> reversedMap) {
        _leftCardinality = leftCardinality;
        _rightCardinality = rightCardinality;
        _map = map;
        _reversedMap = reversedMap;
    }

    @Override
    public final Cardinality leftCardinality() {
        return _leftCardinality;
    }

    @Override
    public final Cardinality rightCardinality() {
        return _rightCardinality;
    }

    @Override
    public Set<R> curryLeft(L key) {
        return curry0(_map.get(key));
    }

    @Override
    public Set<L> curryRight(R key) {
        return curry0(_reversedMap.get(key));
    }

    @Override
    public void insert(L key, R value) {
        Set<L> keys = _reversedMap.get(value);
        Set<R> values = _map.get(key);
        if (keys == null) {
            keys = createKeySet(value);
            _reversedMap.put(value, keys);
        }
        if (values == null) {
            values = createValueSet(key);
            _map.put(key, values);
        }
        values.add(value);
        keys.add(key);
    }

    @Override
    public void remove(L key, R value) {
        Set<R> values = _map.get(key);
        if (values != null) {
            values.remove(value);
            _reversedMap.get(value).remove(key);
        }
    }

    private Set<L> createKeySet(R value) {
        return _leftCardinality.createSet(value, _reversedMap);
    }

    private Set<R> createValueSet(L key) {
        return _rightCardinality.createSet(key, _map);
    }

    private <T> Set<T> curry0(Set<T> set) {
        return set == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(new HashSet<>(set));
    }

}
