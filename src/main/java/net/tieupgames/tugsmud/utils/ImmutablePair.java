package net.tieupgames.tugsmud.utils;

import java.util.Map;
import java.util.Objects;

public final class ImmutablePair<L, R> implements Map.Entry<L, R> {

    private final L _left;
    private final R _right;

    public ImmutablePair(L key, R value) {
        _left = key;
        _right = value;
    }

    public L getLeft() {
        return _left;
    }

    public R getRight() {
        return _right;
    }

    @Override
    public boolean equals(Object o) {
        // as specified by the Map.Entry contract
        if (!(o instanceof Map.Entry)) {
            return false;
        }
        Map.Entry<?, ?> other = (Map.Entry<?, ?>)o;
        return Objects.equals(_left, other.getKey()) && Objects.equals(_right, other.getValue());
    }

    @Override
    public int hashCode() {
        // as specified by the Map.Entry contract
        return Objects.hashCode(_left) ^ Objects.hashCode(_right);
    }

    @Override
    public String toString() {
        return "(" + _left + ", " + _right + ")";
    }

    @Override
    @Deprecated
    public L getKey() {
        return getLeft();
    }

    @Override
    @Deprecated
    public R getValue() {
        return getRight();
    }

    @Override
    public R setValue(R value) {
        throw new UnsupportedOperationException();
    }
}
