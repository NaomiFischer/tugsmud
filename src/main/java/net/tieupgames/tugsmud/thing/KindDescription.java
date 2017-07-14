package net.tieupgames.tugsmud.thing;

import net.tieupgames.tugsmud.parser.OptionalParseField;
import net.tieupgames.tugsmud.parser.ParseEvent;
import net.tieupgames.tugsmud.parser.Registry;

import java.util.function.Supplier;

@ParseEvent(name="Kind", produces=KindOfThing.class)
@SuppressWarnings("unused")
class KindDescription implements Supplier<KindOfThing> {

    @OptionalParseField
    private Class<? extends Thing> thingClass = Thing.class;

    @OptionalParseField
    private boolean special;

    private final String _name;
    private final Registry _registry;

    KindDescription(String name, Registry registry) {
        _name = name;
        _registry = registry;
    }

    Class<? extends Thing> getThingClass() {
        return thingClass;
    }

    boolean isSpecial() {
        return special;
    }

    @Override
    public KindOfThing get() {
        return KindOfThing.get(thingClass, special);
    }
}
