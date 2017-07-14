package net.tieupgames.tugsmud.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class EntryIDTracker {

    private final Map<Integer, Object> _idToEntry = new HashMap<>();
    private final Map<Object, Integer> _entryToId = new HashMap<>();

    EntryIDTracker() {}

    int getId(Object object) {
        return Objects.requireNonNull(_entryToId.get(object), "couldn't find an ID for " + object);
    }

    Object get(int id) {
        return Objects.requireNonNull(_idToEntry.get(id), "couldn't find an entry with ID " + id);
    }

    synchronized final void add(Object object) {
        assert _idToEntry.size() == _entryToId.size();
        int id = object instanceof Identified ? ((Identified)object).idForNewRegistryEntry() : _idToEntry.size();
        _entryToId.put(object, id);
        _idToEntry.put(id, object);
    }
}
