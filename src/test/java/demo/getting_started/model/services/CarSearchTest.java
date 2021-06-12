// **************************************************
//                        GSDK                       
//           Graffica System Development Kit         
//                                                   
//  Release Version: {RELEASE_VERSION}               
//  Copyright: (c) Graffica Ltd {RELEASE_DATE}       
//                                                   
// **************************************************
//  This software is provided under the terms of the 
//        Graffica Software Licence Agreement        
//                                                   
//     THIS HEADER MUST NOT BE ALTERED OR REMOVED    
// **************************************************

package demo.getting_started.model.services;

import demo.getting_started.model.structures.Car;
import demo.getting_started.model.structures.SortableColour;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

public class CarSearchTest {

   private List< Car > carsToSearch;
   private CarSearch systemUnderTest;

   @BeforeEach
   public void initialiseSystemUnderTest() {
      systemUnderTest = new CarSearch();

      carsToSearch = asList(
            new Car( null, "Best Model", "Best Make", null, null, null,
                  new SortableColour( Color.WHITE, "White" )
            ),
            new Car( null, "Worst Model", "Best Make", null, null, null,
                  new SortableColour( Color.WHITE, "White" )
            ),
            new Car( null, "Best Model", "Worst Make", null, null, null,
                  new SortableColour( Color.RED, "Red" )
            ),
            new Car( null, "Worst Model", "Worst Make", null, null, null,
                  new SortableColour( Color.BLUE, "Blue" )
            ),
            new Car( null, "Best Model", "Mid Range Make", null, null, null,
                  new SortableColour( Color.GREEN, "Green" )
            ),
            new Car( null, "Worst Model", "Mid Range Make", null, null, null,
                  new SortableColour( Color.BLUE, "Blue" )
            ),
            new Car( null, null, null, null, null, null, null )
      );
   }

   @Test
   public void shouldMatchMake() {
      assertThat(
            systemUnderTest.search( carsToSearch, "Best Make" ),
            containsInAnyOrder( carsToSearch.get( 0 ), carsToSearch.get( 1 ) )
      );

      assertThat(
            systemUnderTest.search( carsToSearch, "Worst Make" ),
            containsInAnyOrder( carsToSearch.get( 2 ), carsToSearch.get( 3 ) )
      );

      assertThat(
            systemUnderTest.search( carsToSearch, "Make" ),
            containsInAnyOrder(
                  carsToSearch.get( 0 ), carsToSearch.get( 1 ), carsToSearch.get( 2 ),
                  carsToSearch.get( 3 ), carsToSearch.get( 4 ), carsToSearch.get( 5 )
            )
      );
   }

   @Test
   public void shouldMatchModel() {
      assertThat(
            systemUnderTest.search( carsToSearch, "Best Model" ),
            containsInAnyOrder(
                  carsToSearch.get( 0 ), carsToSearch.get( 2 ), carsToSearch.get( 4 ) )
      );

      assertThat(
            systemUnderTest.search( carsToSearch, "Worst Model" ),
            containsInAnyOrder(
                  carsToSearch.get( 1 ), carsToSearch.get( 3 ), carsToSearch.get( 5 )
            )
      );

      assertThat(
            systemUnderTest.search( carsToSearch, "Model" ),
            containsInAnyOrder(
                  carsToSearch.get( 0 ), carsToSearch.get( 1 ), carsToSearch.get( 2 ),
                  carsToSearch.get( 3 ), carsToSearch.get( 4 ), carsToSearch.get( 5 )
            )
      );
   }

   @Test
   public void shouldMatchColour() {
      assertThat(
            systemUnderTest.search( carsToSearch, "White" ),
            containsInAnyOrder(
                  carsToSearch.get( 0 ), carsToSearch.get( 1 ) )
      );

      assertThat(
            systemUnderTest.search( carsToSearch, "Blue" ),
            containsInAnyOrder(
                  carsToSearch.get( 3 ), carsToSearch.get( 5 )
            )
      );
   }

   @Test
   public void shouldMatchAnyWithCommonCharacters() {
      assertThat(
            systemUnderTest.search( carsToSearch, "e" ),
            containsInAnyOrder(
                  carsToSearch.get( 0 ), carsToSearch.get( 1 ), carsToSearch.get( 2 ),
                  carsToSearch.get( 3 ), carsToSearch.get( 4 ), carsToSearch.get( 5 )
            )
      );

      assertThat(
            systemUnderTest.search( carsToSearch, "u" ),
            containsInAnyOrder( carsToSearch.get( 3 ), carsToSearch.get( 5 ) )
      );
   }
   
   @Test
   public void shouldProvideAllWhenNoSearchCriteria() {
      assertThat( systemUnderTest.search( carsToSearch, ""), equalTo( carsToSearch ) );
   }

}