package demo.getting_started.mvc;

import demo.getting_started.beans.BeanResolver;
import demo.getting_started.model.services.CarService;
import demo.getting_started.model.structures.Car;
import demo.getting_started.model.structures.SortableColour;
import demo.getting_started.utility.ExecutionsHandle;
import demo.getting_started.utility.Messages;
import demo.getting_started.utility.PageRedirect;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

/**
 * Provides the controller for making edits to {@link demo.getting_started.model.structures.Car}
 * data accessed with the {@link CarService}.
 */
@VariableResolver( { BeanResolver.class } )
public class CarEditController extends SelectorComposer< Component > {

   @Wire
   private Textbox modelTextBox;
   @Wire
   private Textbox makeTextBox;
   @Wire
   private Intbox priceIntBox;
   @Wire
   private Textbox descriptionTextBox;
   @Wire
   private Textbox previewTextBox;
   @Wire
   private Textbox colourNameTextBox;

   @WireVariable
   private CarService carService;

   private final PageRedirect pageRedirect;
   private final Messages messages;
   private final ExecutionsHandle executionsHandle;

   private Optional< Car > currentlySelectedCar;

   /**
    * Constructs a new {@link CarEditController}.
    */
   public CarEditController() {
      this( new PageRedirect(), new Messages(), new ExecutionsHandle() );
   }

   /**
    * Constructs a new {@link CarEditController}.
    * @param pageRedirect for navigating pages.
    * @param messages     for user information.
    */
   CarEditController(
         PageRedirect pageRedirect, Messages messages,
         ExecutionsHandle executionsHandle
   ) {
      this.pageRedirect = pageRedirect;
      this.messages = messages;
      this.executionsHandle = executionsHandle;
      this.currentlySelectedCar = Optional.empty();
   }

   @Override
   public void doAfterCompose( Component comp ) throws Exception {
      super.doAfterCompose( comp );
      String parameter = executionsHandle.retrieveParameter( "id" );
      currentlySelectedCar = attemptSelectionParameterParse( parameter )
            .flatMap( carService::find );
      currentlySelectedCar.ifPresent( this::populateForSelection );
   }

   /**
    * Attempts to retrieve an optional parameter representing the id of the car to edit.
    * @param parameter id of the car.
    * @return the parsed parameter or empty if not valid.
    */
   private Optional< Integer > attemptSelectionParameterParse( String parameter ) {
      try {
         return Optional.of( Integer.parseInt( parameter ) );
      } catch ( NumberFormatException exception ) {
         //should log in some way - could be coding error or could be curious user...
         return Optional.empty();
      }
   }

   @Listen( "onClick = #returnToDemoButton" )
   public void returnToDemo() {
      pageRedirect.redirectTo( ApplicationPage.DEMO_PAGE );
   }

   /**
    * Submits the car edit performing validating and handling the result.
    */
   @Listen( "onClick = #submitButton" )
   public void submitCarEdit() {
      List< String > validationResults = asList(
            validateStringInput( "Model", modelTextBox.getValue() ),
            validateStringInput( "Make", makeTextBox.getValue() ),
            validateStringInput( "Preview", previewTextBox.getValue() ),
            validateStringInput( "Description", descriptionTextBox.getValue() ),
            validateIntInput( "Price", priceIntBox.getValue() ),
            validateStringInput( "Colour Name", colourNameTextBox.getValue() )
      ).stream()
            .filter( Optional::isPresent )
            .map( Optional::get )
            .collect( Collectors.toList() );

      if ( !validationResults.isEmpty() ) {
         String validationOutput = validationResults.stream()
               .collect( Collectors.joining( ",\n" ) );
         messages.information(
               "Input data is not valid. Please review issues and amend data:\n\n" +
                     validationOutput,
               "Submit Failure"
         );
         return;
      }

      Car carToUpdate = currentlySelectedCar.orElseGet( carService::create );
      carToUpdate.setModel( modelTextBox.getValue() );
      carToUpdate.setMake( makeTextBox.getValue() );
      carToUpdate.setPreview( previewTextBox.getValue() );
      carToUpdate.setDescription( descriptionTextBox.getValue() );
      carToUpdate.setPrice( priceIntBox.getValue() );
      carToUpdate.setColour( new SortableColour( colourNameTextBox.getValue() ) );

      returnToDemo();
   }

   /**
    * Populates the ui with information from the currently selected car.
    * @param currentSelection to update the ui with.
    */
   private void populateForSelection( Car currentSelection ) {
      modelTextBox.setValue( currentSelection.getModel() );
      makeTextBox.setValue( currentSelection.getMake() );
      previewTextBox.setValue( currentSelection.getPreview() );
      descriptionTextBox.setValue( currentSelection.getDescription() );
      priceIntBox.setValue( currentSelection.getPrice() );
      colourNameTextBox.setValue( currentSelection.getColour().getColourDisplayName() );
   }

   /**
    * Common validation performed on string inputs.
    * @param inputName representing what is being validated.
    * @param input     to validate.
    * @return the validation result if negative, otherwise empty.
    */
   private Optional< String > validateStringInput( String inputName, String input ) {
      if ( input == null ) {
         return Optional.of( inputName + ": Value not supplied" );
      }

      if ( input.trim().isEmpty() ) {
         return Optional.of( inputName + ": Empty value supplied" );
      }

      return Optional.empty();
   }

   /**
    * Common validation performed on integer inputs.
    * @param inputName representing what is being validated.
    * @param input     to validate.
    * @return the validation result if negative, otherwise empty.
    */
   private Optional< String > validateIntInput( String inputName, Integer input ) {
      if ( input == null ) {
         return Optional.of( inputName + ": Value not supplied" );
      }

      if ( input < 0 ) {
         return Optional.of( inputName + ": Value must be greater than 0" );
      }

      return Optional.empty();
   }

   /*
    * Ideally we would not have these setters. It's not clear how best to test these elements.
    * There's possibility for wiring on construction, or using set methods, but no success with
    * this thus far. Mockito is capable of these types of things so this should be able to be
    * improved.
    */

   void setCarService( CarService carService ) {
      this.carService = carService;
   }

   void setColourNameTextBox( Textbox colourNameTextBox ) {
      this.colourNameTextBox = colourNameTextBox;
   }

   void setDescriptionTextBox( Textbox descriptionTextBox ) {
      this.descriptionTextBox = descriptionTextBox;
   }

   void setMakeTextBox( Textbox makeTextBox ) {
      this.makeTextBox = makeTextBox;
   }

   void setModelTextBox( Textbox modelTextBox ) {
      this.modelTextBox = modelTextBox;
   }

   void setPreviewTextBox( Textbox previewTextBox ) {
      this.previewTextBox = previewTextBox;
   }

   void setPriceIntBox( Intbox priceIntBox ) {
      this.priceIntBox = priceIntBox;
   }
}
