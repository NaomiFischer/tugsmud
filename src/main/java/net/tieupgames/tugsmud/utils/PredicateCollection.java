package net.tieupgames.tugsmud.utils;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * A {@link Collection} that is also a {@link Predicate}. The predicate indicates
 * whether or not some object is a member of the collection.
 * @param <T> the type of the elements of the collection
 */
public interface PredicateCollection<T> extends Collection<T>, Predicate<T> {

    default boolean test(T t) {
        return contains(t);
    }

}
