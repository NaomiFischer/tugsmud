package net.tieupgames.tugsmud.relation;

import net.tieupgames.tugsmud.utils.SetDecorator;

import java.util.*;

public enum Cardinality {

    ONE {
        @Override <T> Set<T> createSetImpl() {
            return new Cardinality1Set<>();
        }
    },

    MANY {
        @Override <T> Set<T> createSetImpl() {
            return new HashSet<>();
        }
    };

    <T> Set<T> createSet(final Object key, final Map<?, Set<T>> map) {
        return new SetDecorator<T>(createSetImpl()) {
            @Override public boolean remove(Object value) {
                boolean result = super.remove(value);
                if (isEmpty()) {
                    map.remove(key);
                }
                return result;
            }

            @Override public boolean removeAll(Collection<?> c) {
                boolean result = false;
                for(Object o: c) {
                    result = remove(o) || result;
                }
                return result;
            }

            @Override public void clear() {
                map.remove(key);
                super.clear();
            }

            @Override public Iterator<T> iterator() {
                final Iterator<T> iterator = super.iterator();
                return new Iterator<T>() {
                    @Override public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override public T next() {
                        return iterator.next();
                    }

                    @Override public void remove() {
                        iterator.remove();
                        if (isEmpty()) {
                            map.remove(key);
                        }
                    }
                };
            }
        };
    }

    abstract <T> Set<T> createSetImpl();

}
