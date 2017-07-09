package net.tieupgames.tugsmud.relation;

import java.util.function.Predicate;

class RelationWithPredicates<L, R> extends BaseRelation<L, R> {

    private final Predicate<Object> _leftPredicate;
    private final Predicate<Object> _rightPredicate;

    RelationWithPredicates(Cardinality leftCardinality, Cardinality rightCardinality,
                           Predicate<Object> leftPredicate, Predicate<Object> rightPredicate) {
        super(leftCardinality, rightCardinality);
        _leftPredicate = leftPredicate;
        _rightPredicate = rightPredicate;
    }

    @Override
    public void insert(L key, R value) {
        if (!_leftPredicate.test(key)) {
            throw new IllegalArgumentException("left-hand value " + key.toString() + " violates " + _leftPredicate);
        }
        if (!_rightPredicate.test(value)) {
            throw new IllegalArgumentException("left-hand value " + value.toString() + " violates " + _rightPredicate);
        }
        super.insert(key, value);
    }
}
