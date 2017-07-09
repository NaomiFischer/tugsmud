package net.tieupgames.tugsmud.relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationRegistry {

    public static final RelationRegistry INSTANCE = new RelationRegistry(new ArrayList<>(), new HashMap<>());

    private final List<Relation<?, ?>> _idsToRelation;
    private final Map<Relation<?, ?>, Integer> _relationsToId;

    RelationRegistry(List<Relation<?, ?>> idsToRelation, Map<Relation<?, ?>, Integer> relationsToId) {
        _idsToRelation = idsToRelation;
        _relationsToId = relationsToId;
    }

    public int getId(Relation relation) {
        if (_relationsToId.containsKey(relation)) {
            return _relationsToId.get(relation);
        }
        int id = _idsToRelation.size();
        _relationsToId.put(relation, id);
        _idsToRelation.add(relation);
        return id;
    }

    public Relation getRelation(int id) {
        return _idsToRelation.get(id);
    }
}
