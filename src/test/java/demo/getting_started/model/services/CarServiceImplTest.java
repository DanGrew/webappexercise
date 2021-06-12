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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

public class CarServiceImplTest {

   private CarServiceImpl systemUnderTest;

   @BeforeEach
   public void initialiseSystemUnderTest() {
      systemUnderTest = new CarServiceImpl();
   }

   @Test
   public void shouldProvideSomeCarsByDefault() {
      assertThat( systemUnderTest.findAll(), not( empty() ) );
   }

   @Test
   public void shouldAddNewCarWithNoInformation() {
      Car returned = systemUnderTest.create();
      Car newlyAdded = systemUnderTest.findAll().get( systemUnderTest.findAll().size() - 1 );
      assertThat( returned, equalTo( newlyAdded ) );
   }

   @Test
   public void shouldAddNewCar() {
      Car returned = systemUnderTest.create(
            "new model", "new make", "new description", "new preview",
            394873987, new SortableColour( Color.WHITE.toString(), "White" )
      );

      Car newlyAdded = systemUnderTest.findAll().get( systemUnderTest.findAll().size() - 1 );
      assertThat( returned, equalTo( newlyAdded ) );
      assertThat( newlyAdded.getMake(), equalTo( "new make" ) );
      assertThat( newlyAdded.getModel(), equalTo( "new model" ) );
      assertThat( newlyAdded.getDescription(), equalTo( "new description" ) );
      assertThat( newlyAdded.getPreview(), equalTo( "new preview" ) );
      assertThat( newlyAdded.getPrice(), equalTo( 394873987 ) );
   }

   @Test
   public void shouldRemoveCar() {
      Car toRemove = systemUnderTest.findAll().get( 0 );
      systemUnderTest.remove( toRemove );
      assertThat( systemUnderTest.findAll().contains( toRemove ), equalTo( false ) );
   }

}