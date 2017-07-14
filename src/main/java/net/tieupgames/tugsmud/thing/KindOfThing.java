package net.tieupgames.tugsmud.thing;


import net.tieupgames.tugsmud.parser.Identified;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class KindOfThing {

    public static int SPECIAL_KIND_ID = -1;

    private final Constructor<? extends Thing> _constructor;
    private final Map<Integer, Thing> _things;

    static KindOfThing get(Class<? extends Thing> thingClass, boolean special) {
        return special ? new KindOfThing.Special(thingClass) : new KindOfThing(thingClass);
    }

    KindOfThing(Class<? extends Thing> thingClass) {
        if (!Thing.class.isAssignableFrom(thingClass)) {
            throw new IllegalArgumentException(thingClass + " does not extend Thing");
        }
        try {
            _constructor = thingClass.getDeclaredConstructor(int.class, KindOfThing.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(thingClass + " does not declare an (int, KindOfThing) constructor");
        }
        _things = new HashMap<>();
    }

    Thing getThing(int id) {
        return _things.computeIfAbsent(id, this::get0);
    }

    public boolean contains(Thing thing) {
        return this.equals(thing.getKind());
    }

    private Thing get0(int thingId) {
        try {
            return _constructor.newInstance(thingId, this);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static final class Special extends KindOfThing implements Identified {

        Special(Class<? extends Thing> thingClass) {
            super(thingClass);
        }

        @Override public int idForNewRegistryEntry() {
            return SPECIAL_KIND_ID;
        }
    }
}
