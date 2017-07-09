package net.tieupgames.tugsmud.utils;

import java.util.Collection;
import java.util.Iterator;

public class CollectionDecorator<T> implements Collection<T> {

    private final Collection<T> _decorates;

    public CollectionDecorator(Collection<T> decorates) {
        if (decorates == null) {
            throw new NullPointerException("can't decorate a null collection");
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
    public boolean contains(Object o) {
        return _decorates.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return _decorates.iterator();
    }

    @Override
    public Object[] toArray() {
        return _decorates.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return _decorates.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return _decorates.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return _decorates.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return _decorates.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return _decorates.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return _decorates.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return _decorates.retainAll(c);
    }

    @Override
    public void clear() {
        _decorates.clear();
    }

    @Override
    public String toString() {
        return _decorates.toString();
    }

    @Override
    public boolean equals(Object o) {
        return _decorates.equals(o);
    }

    @Override
    public int hashCode() {
        return _decorates.hashCode();
    }

}
