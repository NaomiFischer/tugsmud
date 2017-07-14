package net.tieupgames.tugsmud.relation;

import java.util.Set;

public interface Relation<L, R> {

    Cardinality leftCardinality();

    Cardinality rightCardinality();

    Set<R> curryLeft(L key);

    Set<L> curryRight(R key);

    void insert(L key, R value);

    void remove(L key, R value);

    default boolean contains(L left, R right) {
        Set<R> curried = curryLeft(left);
        return curried != null && curried.contains(right);
    }
}
