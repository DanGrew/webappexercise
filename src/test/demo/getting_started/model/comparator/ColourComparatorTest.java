package demo.getting_started.model.comparator;

import demo.getting_started.model.structures.Car;
import demo.getting_started.model.structures.SortableColour;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

public class ColourComparatorTest {

   private ColourComparator systemUnderTest;

   @BeforeEach
   public void initialiseSystemUnderTest() {
      systemUnderTest = new ColourComparator();
   }

   @Test
   public void shouldSortColourAscending() {
      Car black = new Car(null, null, null, null, null, null, new SortableColour(Color.BLACK, "Black"));
      Car white = new Car(null, null, null, null, null, null, new SortableColour(Color.WHITE, "White"));

      assertThat(systemUnderTest.compare(black, white), lessThan(0));
      assertThat(systemUnderTest.compare(white, black), greaterThan(0));

      assertThat(systemUnderTest.compare(black, black), equalTo(0));
   }

   @Test
   public void shouldInterpretNullAsEmptyName() {
      Car black = new Car(null, null, null, null, null, null, null);
      Car white = new Car(null, null, null, null, null, null, new SortableColour(Color.WHITE, "White"));

      assertThat(systemUnderTest.compare(black, white), lessThan(0));
      assertThat(systemUnderTest.compare(white, black), greaterThan(0));

      assertThat(systemUnderTest.compare(black, black), equalTo(0));
   }
}
