package net.tieupgames.tugsmud.thing;

import net.tieupgames.tugsmud.parser.OptionalParseField;
import net.tieupgames.tugsmud.parser.ParseEvent;

import java.util.function.Supplier;

@ParseEvent(name="Kind", produces=KindOfThing.class)
@SuppressWarnings("unused")
class KindDescription implements Supplier<KindOfThing> {

    @OptionalParseField
    Class<? extends Thing> thingClass = Thing.class;

    @OptionalParseField
    boolean special;

    final String name;
    final KindFactory factory;

    KindDescription(String name) {
        this(name, KindFactory.INSTANCE);
    }

    KindDescription(String name, KindFactory factory) {
        this.name = name;
        this.factory = factory;
    }

    @Override
    public KindOfThing get() {
        if (special) {
            return factory.createSpecialKind(thingClass);
        }
        return factory.createKind(thingClass);
    }
}
