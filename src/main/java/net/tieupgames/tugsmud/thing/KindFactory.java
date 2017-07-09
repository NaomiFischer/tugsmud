package net.tieupgames.tugsmud.thing;

import java.util.ArrayList;
import java.util.List;

public class KindFactory {

    public static final KindFactory INSTANCE = new KindFactory();

    static final int SPECIAL_ID = -1;

    private KindOfThing _specialKind = null;
    private final List<KindOfThing> _kinds = new ArrayList<>();

    public synchronized KindOfThing createKind(Class<? extends Thing> thingClass) {
        int kindId = _kinds.size();
        KindOfThing result = new KindOfThing(thingClass, kindId);
        _kinds.add(result);
        return result;
    }

    public KindOfThing getKind(int kindId) {
        if (kindId == SPECIAL_ID) {
            return getSpecialKind();
        }
        if (kindId >= _kinds.size()) {
            throw new IndexOutOfBoundsException("unknown kindId: " + kindId);
        }
        return _kinds.get(kindId);
    }

    public synchronized KindOfThing createSpecialKind(Class<? extends Thing> thingClass) {
        if (_specialKind != null) {
            throw new IllegalStateException("already have a special kind");
        }
        _specialKind = new KindOfThing(thingClass, SPECIAL_ID);
        return _specialKind;
    }

    public KindOfThing getSpecialKind() {
        if (_specialKind == null) {
            throw new IllegalStateException("no special kind is registered");
        }
        return _specialKind;
    }
}
