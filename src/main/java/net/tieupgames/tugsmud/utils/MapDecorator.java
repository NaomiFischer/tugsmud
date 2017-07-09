package net.tieupgames.tugsmud.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MapDecorator<K, V> implements Map<K, V> {

    private final Map<K, V> _decorates;

    public MapDecorator(Map<K, V> decorates) {
        if (decorates == null) {
            throw new NullPointerException("can't decorate a null map");
        }
        _decorates = decorates;
    }

    @Override
    public int size() {
        return _decorates.size();
    }

    @Override
    public boolean isEmpty() {
        return _decorates.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return _decorates.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return _decorates.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return _decorates.get(key);
    }

    @Override
    public V put(K key, V value) {
        return _decorates.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return _decorates.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        _decorates.putAll(m);
    }

    @Override
    public void clear() {
        _decorates.clear();
    }

    @Override
    public Set<K> keySet() {
        return _decorates.keySet();
    }

    @Override
    public Collection<V> values() {
        return _decorates.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return _decorates.entrySet();
    }

    @Override
    public String toString() {
        return _decorates.toString();
    }

    @Override
    public int hashCode() {
        return _decorates.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return _decorates.equals(o);
    }
}
