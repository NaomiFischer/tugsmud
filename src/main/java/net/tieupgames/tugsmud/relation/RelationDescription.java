package net.tieupgames.tugsmud.relation;

import net.tieupgames.tugsmud.parser.OptionalParseField;
import net.tieupgames.tugsmud.parser.ParseEvent;
import net.tieupgames.tugsmud.parser.ParseFieldWithConversionFunction;
import net.tieupgames.tugsmud.parser.Registry;
import net.tieupgames.tugsmud.thing.KindOfThing;
import net.tieupgames.tugsmud.thing.Thing;
import org.json.JSONObject;

import java.util.function.Predicate;
import java.util.function.Supplier;

@ParseEvent(name="Relation", produces=Relation.class)
@SuppressWarnings("unused")
class RelationDescription implements Supplier<Relation> {

    Cardinality leftCardinality;
    Cardinality rightCardinality;

    @OptionalParseField
    @ParseFieldWithConversionFunction("predicateFor")
    Predicate<Object> leftPredicate = anything -> true;

    @OptionalParseField
    @ParseFieldWithConversionFunction("predicateFor")
    Predicate<Object> rightPredicate = anything -> true;

    private final Registry _registry;

    RelationDescription(String name, Registry registry) {
        _registry = registry;
    }

    @Override
    public Relation get() {
        return new RelationWithPredicates(leftCardinality, rightCardinality, leftPredicate, rightPredicate);
    }

    Predicate<Object> predicateFor(JSONObject json, String key) {
        return new PredicateForKindWithName(json.get(key).toString());
    }

    private final class PredicateForKindWithName implements Predicate<Object> {

        private final String _kindName;
        private KindOfThing _kind;

        PredicateForKindWithName(String kindName) {
            _kindName = kindName;
        }

        @Override
        public boolean test(Object o) {
            if (!(o instanceof Thing)) {
                return false;
            }
            if (_kind == null) {
                _kind = _registry.get(_kindName, KindOfThing.class);
            }
            return _kind.contains(((Thing)o));
        }

        @Override
        public String toString() {
            return _kindName;
        }
    }
}
