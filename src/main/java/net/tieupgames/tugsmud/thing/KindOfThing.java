package net.tieupgames.tugsmud.thing;


import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class KindOfThing {

    private final int _kindId;
    private final Constructor<? extends Thing> _constructor;
    private final Map<Integer, Thing> _things;

    KindOfThing(Class<? extends Thing> thingClass, int id) {
        if (!Thing.class.isAssignableFrom(thingClass)) {
            throw new IllegalArgumentException(thingClass + " does not extend Thing");
        }
        try {
            _constructor = thingClass.getDeclaredConstructor(int.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(thingClass + " does not declare a constructor taking a single int");
        }
        _kindId = id;
        _things = new HashMap<>();
    }

    Thing get(int id) {
        assert contains(id) : "Thing created with wrong KindOfThing: our ID is " + _kindId + ", incoming ID is " + Things.extractKindId(id);
        return _things.computeIfAbsent(id, this::get0);
    }

    public boolean contains(Thing thing) {
        return contains(thing.getId());
    }

    public boolean contains(int id) {
        return Things.extractKindId(id) == _kindId;
    }

    public int getId() {
        return _kindId;
    }

    private Thing get0(int thingId) {
        try {
            return _constructor.newInstance(thingId);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
