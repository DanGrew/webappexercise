package demo.getting_started.model.comparator;

import demo.getting_started.model.structures.Car;

/**
 * Concrete implementation of {@link CarComparator} specifically for {@link Car#getMake()}.
 */
public class MakeComparator extends CarComparator<String> {

   /**
     * Constructs a new {@link MakeComparator}.
     */
   public MakeComparator() {
      super(Car::getMake, "");
   }
}
