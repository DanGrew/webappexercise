package demo.getting_started.model.comparator;

import demo.getting_started.tutorial.Car;

/**
 * Concrete implementation of {@link CarComparator} specifically for {@link Car#getModel()}.
 */
public class ModelComparator extends CarComparator<String> {

   /**
     * Constructs a new {@link ModelComparator}.
     */
   public ModelComparator() {
      super(Car::getModel, "");
   }
}
