package demo.getting_started.model.structures;

import javafx.scene.paint.Color;

import java.util.Objects;

/**
 * Encapsulates a {@link Color} with a name. The name is used for display purposes such as sorting which is not always trivial for colours.
 */
public class SortableColour implements Comparable<SortableColour> {

   private final Color colour;
   private final String colourName;

   /**
    * Constructs a new {@link SortableColour}.
    *
    * @param colour     the actual colour.
    * @param colourName a description for the colour.
    */
   public SortableColour(Color colour, String colourName) {
      this.colour = Objects.requireNonNull(colour);
      this.colourName = Objects.requireNonNull(colourName);
   }

   public Color getColour() {
      return colour;
   }

   public String getColourName() {
      return colourName;
   }

   @Override
   public int compareTo(SortableColour o) {
      return getColourName().compareTo(o.getColourName());
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
      return Objects.equals(colour, that.colour) && Objects.equals(colourName, that.colourName);
   }

   @Override
   public int hashCode() {
      return Objects.hash(colour, colourName);
   }
}
