package demo.getting_started.model.comparator;

import demo.getting_started.model.structures.Car;
import demo.getting_started.model.structures.SortableColour;

/**
 * Concrete implementation of {@link CarComparator} specifically for {@link Car#getColour()}.
 */
public class ColourComparator extends CarComparator<SortableColour> {

   /**
    * Constructs a new {@link ColourComparator}.
    */
   public ColourComparator() {
      super(Car::getColour, new SortableColour(""));
   }
}
