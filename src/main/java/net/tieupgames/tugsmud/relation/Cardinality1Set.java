package net.tieupgames.tugsmud.relation;

import java.util.*;

final class Cardinality1Set<T> implements Set<T> {

    private T _element;
    private boolean _hasAnElement = false;

    @Override
    public int size() {
        return _hasAnElement ? 1 : 0;
    }

    @Override
    public boolean isEmpty() {
        return !_hasAnElement;
    }

    @Override
    public boolean contains(Object o) {
        return Objects.equals(o, _element);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private T _next = _element;
            private T _recent;
            private boolean _nextWasCalled;

            @Override public boolean hasNext() {
                return !_nextWasCalled;
            }

            @Override public T next() {
                if (_nextWasCalled) {
                    throw new NoSuchElementException();
                }
                if (!_next.equals(_element)) {
                    throw new ConcurrentModificationException();
                }
                _recent = _next;
                _next = null;
                _nextWasCalled = true;
                return _recent;
            }

            @Override public void remove() {
                if (!_nextWasCalled) {
                    throw new IllegalStateException();
                }
                if (!Objects.equals(_recent, _element)) {
                    throw new ConcurrentModificationException();
                }
                clear();
            }
        };
    }

    @Override
    public Object[] toArray() {
        // if the set is empty, return an empty array
        // otherwise, return the set of one thing, which is just our single element
        return _hasAnElement ? new Object[] {_element} : new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        // defer to the nullary toArray, and use Arrays.asList to get it into the proper form
        return Arrays.asList(toArray()).toArray(a);
    }

    @Override
    public boolean add(T t) {
        // adding the same thing we already contain is the only way this can not modify the set
        // corner case: if we're an empty set, and add(null) is called, that's actually a modification
        // since having no elements and having only a null element are different (eg. for isEmpty())
        if (_hasAnElement && Objects.equals(t, _element)) {
            return false;
        }
        _hasAnElement = true;
        _element = t;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        // removing anything from an empty set is idempotent
        if (!_hasAnElement) {
            return false;
        }
        // if we're not an empty set, we need to make sure the element we're removing is in the set
        // since we have at most one element (and we ruled out the case of having 0 above), this is easy;
        if (Objects.equals(o, _element)) {
            clear();
            return true;
        }
        // okay, it's a different object
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // if the other collection is empty, we contain everything
        // making this check explicit prevents corner cases later
        if (c.isEmpty()) {
            return true;
        }
        return _hasAnElement && c.size() == 1 && c.contains(_element);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException(
                "addAll is not well-defined on a set that guarentees a maximum size (of 1)");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // removing from an empty set is always idempotent
        if (!_hasAnElement) {
            return false;
        }

        // if the other set contains our single element, then this set is already the intersection, and we're good
        if (c.contains(_element)) {
            return false;
        }

        // no elements in common, so clear this set
        clear();
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // removing from an empty set is always idempotent
        if (!_hasAnElement) {
            return false;
        }

        // either we're a subset of the other collection (and become empty),
        // or there are no elements in common (and nothing happens)
        if (c.contains(_element)) {
            clear();
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        _hasAnElement = false;
        _element = null;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Set)) {
            return false;
        }
        Set<?> other = (Set<?>)o;
        if (!_hasAnElement) {
            return other.isEmpty();
        }
        return other.size() == 1 && Objects.equals(_element, other.iterator().next());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(_element);
    }
}
