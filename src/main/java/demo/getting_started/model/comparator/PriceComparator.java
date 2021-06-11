package demo.getting_started.model.comparator;

import demo.getting_started.tutorial.Car;

/**
 * Concrete implementation of {@link CarComparator} specifically for {@link Car#getPrice()}.
 */
public class PriceComparator extends CarComparator<Integer> {

   /**
     * Constructs a new {@link PriceComparator}.
     */
   public PriceComparator() {
      super(Car::getPrice, 0);
   }
}
