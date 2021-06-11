package demo.getting_started.model.comparator;

import demo.getting_started.model.structures.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

public class PriceComparatorTest {

   private PriceComparator systemUnderTest;

   @BeforeEach
   public void initialiseSystemUnderTest() {
      systemUnderTest = new PriceComparator();
   }

   @Test
   public void shouldSortPriceAscending() {
      Car lowerPrice = new Car(null, null, null, null, null, 2000);
      Car higherPrice = new Car(null, null, null, null, null, 3000);

      assertThat(systemUnderTest.compare(lowerPrice, higherPrice), lessThan(0));
      assertThat(systemUnderTest.compare(higherPrice, lowerPrice), greaterThan(0));

      assertThat(systemUnderTest.compare(lowerPrice, lowerPrice), equalTo(0));
   }

   @Test
   public void shouldInterpretNullAsZero() {
      Car lowerPrice = new Car(null, null, null, null, null, null);
      Car higherPrice = new Car(null, null, null, null, null, 3000);

      assertThat(systemUnderTest.compare(lowerPrice, higherPrice), equalTo(-1));
      assertThat(systemUnderTest.compare(higherPrice, lowerPrice), equalTo(1));

      assertThat(systemUnderTest.compare(lowerPrice, lowerPrice), equalTo(0));
   }
}
