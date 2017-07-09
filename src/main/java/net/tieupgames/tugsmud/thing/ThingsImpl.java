package net.tieupgames.tugsmud.thing;

class ThingsImpl {

    private final KindFactory _kindFactory;

    ThingsImpl(KindFactory kindFactory) {
        _kindFactory = kindFactory;
    }

    Thing getThing(int thingId) {
        KindOfThing kind;
        if (Things.extractSpecial(thingId) != 0) {
            kind = _kindFactory.getSpecialKind();
        } else {
            int kindId = Things.extractKindId(thingId);
            kind = _kindFactory.getKind(kindId);
        }
        return kind.get(thingId);
    }

}
