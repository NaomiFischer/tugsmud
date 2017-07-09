package net.tieupgames.tugsmud.thing;

public class Things {

    public static final int SPECIAL_BITS = 1;
    public static final int KIND_ID_BITS = 12;
    public static final int ARBITRARY_ID_BITS = 32 - KIND_ID_BITS - 1;

    public static final int FIRST_ARBITRARY_ID = 1 << KIND_ID_BITS;
    public static final int FIRST_SPECIAL_ID = 1 << (32 - SPECIAL_BITS);

    public static final int SPECIAL_MASK = ~((1 << (32 - SPECIAL_BITS)) - 1);
    public static final int KIND_ID_MASK = ((1 << KIND_ID_BITS) - 1);
    public static final int ARBITRARY_ID_MASK = ~(KIND_ID_MASK);

    private static final ThingsImpl IMPL = new ThingsImpl(KindFactory.INSTANCE);

    public static Thing getThing(int thingId) {
        return IMPL.getThing(thingId);
    }

    public static int extractSpecial(int id) {
        return id & SPECIAL_MASK;
    }

    public static int extractKindId(int id) {
        assert extractSpecial(id) == 0;
        return id & KIND_ID_MASK;
    }

    public static int extractArbitraryId(int id) {
        assert extractSpecial(id) == 0;
        return id & ARBITRARY_ID_MASK;
    }
}
