package net.tieupgames.tugsmud.thing;

public class Thing {

   protected final int id;

   public Thing(int id) {
       this.id = id;
   }

   public final int getId() {
       return id;
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

}
