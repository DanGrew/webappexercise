package demo.getting_started.model.comparator;

import demo.getting_started.model.structures.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

public class ModelComparatorTest {

   private ModelComparator systemUnderTest;

   @BeforeEach
   public void initialiseSystemUnderTest() {
      systemUnderTest = new ModelComparator();
   }

   @Test
   public void shouldSortModelAscending() {
      Car bestModel = new Car(null, "Best Model", null, null, null, null, null);
      Car worstModel = new Car(null, "Worst Model", null, null, null, null, null);

      assertThat(systemUnderTest.compare(bestModel, worstModel), lessThan(0));
      assertThat(systemUnderTest.compare(worstModel, bestModel), greaterThan(0));

      assertThat(systemUnderTest.compare(bestModel, bestModel), equalTo(0));
   }

   @Test
   public void shouldInterpretNoModelAsEmpty() {
      Car bestModel = new Car(null, null, null, null, null, null, null);
      Car worstModel = new Car(null, "Worst Model", null, null, null, null, null);

      assertThat(systemUnderTest.compare(bestModel, worstModel), lessThan(0));
      assertThat(systemUnderTest.compare(worstModel, bestModel), greaterThan(0));

      assertThat(systemUnderTest.compare(bestModel, bestModel), equalTo(0));
   }
}
