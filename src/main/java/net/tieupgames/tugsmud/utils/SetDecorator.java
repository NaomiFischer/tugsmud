package net.tieupgames.tugsmud.utils;

import java.util.Set;

public class SetDecorator<T> extends CollectionDecorator<T> implements Set<T> {

    public SetDecorator(Set<T> set) {
        super(set);
    }

}
