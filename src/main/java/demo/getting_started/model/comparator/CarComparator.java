package demo.getting_started.model.comparator;

import demo.getting_started.tutorial.Car;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

/**
 * Defines a base {@link Comparator} for a specific {@link Car} property.
 *
 * @param <ComparableTypeT> the type of the parameter being compared.
 */
public class CarComparator<ComparableTypeT extends Comparable<ComparableTypeT>> implements Comparator<Car> {

   private final Function<Car, ComparableTypeT> dataMapper;
   private final ComparableTypeT defaultValue;

   /**
    * Constructs a new {@link CarComparator}.
    *
    * @param dataMapper   function to extract the comparable property from the {@link Car}.
    * @param defaultValue value to use if the property is not defined on the {@link Car}.
    */
   public CarComparator(Function<Car, ComparableTypeT> dataMapper, ComparableTypeT defaultValue) {
      this.dataMapper = dataMapper;
      this.defaultValue = defaultValue;
   }

   @Override
   public int compare(Car o1, Car o2) {
      ComparableTypeT firstValue = Optional.ofNullable(dataMapper.apply(o1)).orElse(defaultValue);
      ComparableTypeT secondValue = Optional.ofNullable(dataMapper.apply(o2)).orElse(defaultValue);
      return firstValue.compareTo(secondValue);
   }
}
