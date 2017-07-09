package net.tieupgames.tugsmud.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A {@link Set} that is also a {@link Predicate}. The predicate indicates
 * whether or not some object is a member of the collection.
 * @param <T> the type of the elements of the collection
 */
public class PredicateSet<T> extends SetDecorator<T> implements PredicateCollection<T> {

    /**
     * Constructs an empty predicate set. The set behaves as if it were a {@link HashSet}.
     */
    public PredicateSet() {
        super(new HashSet<>());
    }

    /**
     * Constructs a predicate set that is a copy of some other set. The set behaves as if
     * it were a {@link HashSet}.
     * @param set the set to copy
     * @see #view(Set) if a view, rather than a copy, is desired
     */
    public PredicateSet(Set<T> set) {
        super(new HashSet<>(set));
    }

    /**
     * Returns a predicate view of some other set.
     * @param set the set to get a view of
     * @return a predicate view of that set
     */
    public static <T> PredicateSet<T> view(Set<T> set) {
        return new PredicateSet<>(set, null);
    }

    /**
     * Implementation for {@link #view(Set)}.
     * @param set the set to get a view of
     * @param disambiguate unused; avoids signature collision with the copy constructor
     */
    @SuppressWarnings("unused")
    private PredicateSet(Set<T> set, Void disambiguate) {
        super(set);
    }

}
