package net.tieupgames.tugsmud.thing;

import net.tieupgames.tugsmud.parser.Identified;
import net.tieupgames.tugsmud.parser.Registry;

public class Thing implements Identified {

   protected final int id;
   protected final KindOfThing kind;

   public Thing(int id, KindOfThing kind) {
       this.id = id;
       this.kind = kind;
   }

   public Thing(int id, Registry registry) {
       this.id = id;
       int kindId;
       if (Things.extractSpecial(id) != 0) {
           kindId = KindOfThing.SPECIAL_KIND_ID;
       } else {
           kindId = Things.extractKindId(id);
       }
       kind = registry.get(kindId, KindOfThing.class);
   }

   public final int getId() {
       return id;
   }

   public KindOfThing getKind() {
       return kind;
   }

   @Override
   public final int hashCode() {
      return getId();
   }

   @Override
   public final boolean equals(Object o) {
      if (o instanceof Thing) {
          boolean result = id == ((Thing)o).id;
          assert !result || getClass() == o.getClass() :
              "Things with same ID aren't the same class: " + getClass() + " vs. " + o.getClass();
          return result;
      }
      return false;
   }

    @Override
    public int idForNewRegistryEntry() {
       return getId();
    }
}
