package demo.getting_started.model.comparator;

import demo.getting_started.tutorial.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

public class MakeComparatorTest {

   private MakeComparator systemUnderTest;

   @BeforeEach
   public void initialiseSystemUnderTest() {
      systemUnderTest = new MakeComparator();
   }

   @Test
   public void shouldSortMakeAscending() {
      Car bestMake = new Car(null, null, "Best Make", null, null, null);
      Car worstMake = new Car(null, null, "Worst Make", null, null, null);

      assertThat(systemUnderTest.compare(bestMake, worstMake), lessThan(0));
      assertThat(systemUnderTest.compare(worstMake, bestMake), greaterThan(0));

      assertThat(systemUnderTest.compare(bestMake, bestMake), equalTo(0));
   }

   @Test
   public void shouldInterpretNoMakeAsEmpty() {
      Car bestMake = new Car(null, null, null, null, null, null);
      Car worstMake = new Car(null, null, "Worst Make", null, null, null);

      assertThat(systemUnderTest.compare(bestMake, worstMake), lessThan(0));
      assertThat(systemUnderTest.compare(worstMake, bestMake), greaterThan(0));

      assertThat(systemUnderTest.compare(bestMake, bestMake), equalTo(0));
   }
}
