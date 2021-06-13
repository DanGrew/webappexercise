package demo.getting_started.model.structures;

import java.util.Objects;

/**
 * Represents a colour as a name. The name is used for display purposes such as
 * sorting which is not always trivial for colours.
 */
public class SortableColour implements Comparable< SortableColour > {

   private final String colourDisplayName;

   /**
    * Constructs a new {@link SortableColour}.
    * @param colourDisplayName a description for the colour.
    */
   public SortableColour( String colourDisplayName ) {
      this.colourDisplayName = Objects.requireNonNull( colourDisplayName );
   }

   public String getColourDisplayName() {
      return colourDisplayName;
   }

   @Override
   public int compareTo( SortableColour o ) {
      return getColourDisplayName().compareTo( o.getColourDisplayName() );
   }

   @Override
   public boolean equals( Object o ) {
      if ( this == o ) {
         return true;
      }
      if ( o == null || getClass() != o.getClass() ) {
         return false;
      }
      SortableColour that = ( SortableColour ) o;
      return Objects.equals( colourDisplayName, that.colourDisplayName );
   }

   @Override
   public int hashCode() {
      return Objects.hash( colourDisplayName );
   }
}
