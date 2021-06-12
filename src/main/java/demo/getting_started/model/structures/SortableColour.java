package demo.getting_started.model.structures;

import javafx.scene.paint.Color;

import java.util.Objects;

/**
 * Encapsulates a {@link Color} with a name. The name is used for display purposes such as sorting which is not always trivial for colours.
 */
public class SortableColour implements Comparable<SortableColour> {

   private final String colour;
   private final String colourDisplayName;

   /**
    * Constructs a new {@link SortableColour}.
    *
    * @param colour     the actual colour.
    * @param colourDisplayName a description for the colour.
    */
   public SortableColour(String colour, String colourDisplayName ) {
      this.colour = Objects.requireNonNull(colour);
      this.colourDisplayName = Objects.requireNonNull( colourDisplayName );
   }

   public String getColour() {
      return colour;
   }

   public String getColourDisplayName() {
      return colourDisplayName;
   }

   @Override
   public int compareTo(SortableColour o) {
      return getColourDisplayName().compareTo(o.getColourDisplayName());
   }

   @Override
   public boolean equals(Object o) {
      if(this == o) {
         return true;
      }
      if(o == null || getClass() != o.getClass()) {
         return false;
      }
      SortableColour that = (SortableColour) o;
      return Objects.equals(colour, that.colour) && Objects.equals( colourDisplayName, that.colourDisplayName );
   }

   @Override
   public int hashCode() {
      return Objects.hash(colour, colourDisplayName );
   }
}
