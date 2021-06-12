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

package demo.getting_started.mvc;

import demo.getting_started.model.services.CarService;
import demo.getting_started.model.structures.SortableColour;
import demo.getting_started.utility.Messages;
import demo.getting_started.utility.PageRedirect;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zkoss.zkex.zul.Colorbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;

import static org.mockito.Mockito.*;

@ExtendWith( MockitoExtension.class )
public class CarEditControllerTest {

   @Mock
   private PageRedirect pageRedirect;
   @Mock
   private Messages messages;

   @Mock
   private CarService carService;
   @Mock
   private Textbox modelTextBox;
   @Mock
   private Textbox makeTextBox;
   @Mock
   private Intbox priceIntBox;
   @Mock
   private Textbox descriptionTextBox;
   @Mock
   private Textbox previewTextBox;
   @Mock
   private Textbox colourNameTextBox;
   @Mock
   private Colorbox colourChooserBox;

   private CarEditController systemUnderTest;

   @BeforeEach
   public void initialiseSystemUnderTest() {
      systemUnderTest = new CarEditController( pageRedirect, messages );

      systemUnderTest.setCarService( carService );
      systemUnderTest.setColourChooserBox( colourChooserBox );
      systemUnderTest.setColourNameTextBox( colourNameTextBox );
      systemUnderTest.setDescriptionTextBox( descriptionTextBox );
      systemUnderTest.setMakeTextBox( makeTextBox );
      systemUnderTest.setModelTextBox( modelTextBox );
      systemUnderTest.setPreviewTextBox( previewTextBox );
      systemUnderTest.setPriceIntBox( priceIntBox );

      lenient().when( colourChooserBox.getValue() ).thenReturn( "any colour" );
      lenient().when( colourNameTextBox.getValue() ).thenReturn( "any colour name" );
      lenient().when( descriptionTextBox.getValue() ).thenReturn( "any description" );
      lenient().when( makeTextBox.getValue() ).thenReturn( "any make" );
      lenient().when( modelTextBox.getValue() ).thenReturn( "any model" );
      lenient().when( previewTextBox.getValue() ).thenReturn( "any preview" );
      lenient().when( priceIntBox.getValue() ).thenReturn( 100 );
   }

   @Test
   public void shouldRedirectToDemoWhenRequested() {
      systemUnderTest.returnToDemo();
      verify( pageRedirect ).redirectTo( ApplicationPage.DEMO_PAGE );
   }

   @Test
   public void shouldHandleInvalidModel() {
      when( modelTextBox.getValue() ).thenReturn( null );
      systemUnderTest.submitCarEdit();
      verify( messages ).information(
            "Input data is not valid. Please review issues and amend data:\n\n" +
                  "Model: Value not supplied",
            "Submit Failure"
      );
   }

   @Test
   public void shouldHanleInvalidMake() {
      when( makeTextBox.getValue() ).thenReturn( null );
      systemUnderTest.submitCarEdit();
      verify( messages ).information(
            "Input data is not valid. Please review issues and amend data:\n\n" +
                  "Make: Value not supplied",
            "Submit Failure"
      );
   }

   @Test
   public void shouldHandleInvalidDescription() {
      when( descriptionTextBox.getValue() ).thenReturn( null );
      systemUnderTest.submitCarEdit();
      verify( messages ).information(
            "Input data is not valid. Please review issues and amend data:\n\n" +
                  "Description: Value not supplied",
            "Submit Failure"
      );
   }

   @Test
   public void shouldHandleInvalidPreview() {
      when( previewTextBox.getValue() ).thenReturn( null );
      systemUnderTest.submitCarEdit();
      verify( messages ).information(
            "Input data is not valid. Please review issues and amend data:\n\n" +
                  "Preview: Value not supplied",
            "Submit Failure"
      );
   }

   @Test
   public void shouldHandleInvalidPrice() {
      when( priceIntBox.getValue() ).thenReturn( -1 );
      systemUnderTest.submitCarEdit();
      verify( messages ).information(
            "Input data is not valid. Please review issues and amend data:\n\n" +
                  "Price: Value must be greater than 0",
            "Submit Failure"
      );
   }

   @Test
   public void shouldHandleMultipleInvalidInputs() {
      when( modelTextBox.getValue() ).thenReturn( null );
      when( makeTextBox.getValue() ).thenReturn( null );
      when( descriptionTextBox.getValue() ).thenReturn( null );
      when( previewTextBox.getValue() ).thenReturn( null );
      when( priceIntBox.getValue() ).thenReturn( -1 );
      systemUnderTest.submitCarEdit();
      verify( messages ).information(
            "Input data is not valid. Please review issues and amend data:\n\n" +
                  "Model: Value not supplied,\n" +
                  "Make: Value not supplied,\n" +
                  "Preview: Value not supplied,\n" +
                  "Description: Value not supplied,\n" +
                  "Price: Value must be greater than 0",
            "Submit Failure"
      );
   }

   @Test
   public void shouldCreateCarForValidInputAndReturn() {
      systemUnderTest.submitCarEdit();
      verify( carService ).create(
            modelTextBox.getValue(),
            makeTextBox.getValue(),
            descriptionTextBox.getValue(),
            previewTextBox.getValue(),
            priceIntBox.getValue(),
            new SortableColour( Color.WHITE, colourNameTextBox.getValue() )
      );
      verify( pageRedirect ).redirectTo( ApplicationPage.DEMO_PAGE );
   }
}